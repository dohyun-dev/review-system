package com.dohyundev.review.review.command.domain.answer

import com.dohyundev.review.review.command.domain.review.ReviewItem
import java.math.BigDecimal
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
abstract class ReviewAnswerItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val reviewItemId: Long,
) {
    fun hasValue(): Boolean = true

    fun calculateScore(reviewItem: ReviewItem): BigDecimal = BigDecimal.ZERO

    abstract fun validate(reviewItem: ReviewItem)
}
