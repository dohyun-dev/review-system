package com.dohyundev.review.review.command.domain.group

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotClosedException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotInProgressException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotPreparingException
import jakarta.persistence.*
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
class ReviewGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    name: String,

    description: String? = null,

    period: DateRange? = null,

    targetPeriod: DateRange? = null,
) : AbstractAggregateRoot<ReviewGroup>() {

    @Column(nullable = false, length = 50)
    var name: String = name
        protected set(value) {
            require(value.isNotBlank()) { "리뷰명은 필수입니다." }
            require(value.length <= 25) { "리뷰명은 25자 이내여야 합니다." }
            field = value
        }

    @Column(nullable = true, length = 100)
    var description: String? = description
        protected set(value) {
            require(value == null || value.length <= 50) { "리뷰 설명은 50자 이내여야 합니다." }
            field = value
        }

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "from", column = Column(name = "period_from")),
        AttributeOverride(name = "to", column = Column(name = "period_to")),
    )
    var period: DateRange? = period
        protected set

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "from", column = Column(name = "target_period_from")),
        AttributeOverride(name = "to", column = Column(name = "target_period_to")),
    )
    var targetPeriod: DateRange? = targetPeriod
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReviewGroupStatus = ReviewGroupStatus.PREPARING
        protected set

    init {
        this.name = name
        this.description = description
    }

    fun isPreparing() = status == ReviewGroupStatus.PREPARING

    fun isInProgress() = status == ReviewGroupStatus.IN_PROGRESS

    fun isClosed() = status == ReviewGroupStatus.CLOSED

    fun checkIsPreparing() {
        if (!isPreparing()) throw ReviewGroupNotPreparingException()
    }

    fun update(name: String, description: String?, period: DateRange?, targetPeriod: DateRange?) {
        checkIsPreparing()
        this.name = name
        this.description = description
        this.period = period
        this.targetPeriod = targetPeriod
    }

    fun start(checker: ReviewGroupStartableChecker) {
        checkIsPreparing()

        checker.checkStartable(this)

        status = ReviewGroupStatus.IN_PROGRESS
    }

    fun close() {
        if (!isInProgress()) throw ReviewGroupNotInProgressException()

        status = ReviewGroupStatus.CLOSED

        registerEvent(ReviewGroupClosedEvent(this))
    }

    fun reopen() {
        if (!isClosed()) throw ReviewGroupNotClosedException()

        status = ReviewGroupStatus.IN_PROGRESS

        registerEvent(ReviewGroupReopenedEvent(this))
    }

    fun events(): Collection<Any> = domainEvents()
}
