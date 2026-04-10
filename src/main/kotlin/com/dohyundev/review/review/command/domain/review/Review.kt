package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import jakarta.persistence.*

@Entity
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    val reviewGroup: ReviewGroup,

    type: ReviewType,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "review_id", nullable = false)
    val sections: MutableList<ReviewSection> = mutableListOf(),
) {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: ReviewType = type
        protected set

    init {
        reviewGroup.checkIsPreparing()
        require(sections.isNotEmpty()) { "섹션은 최소 1개 이상이어야 합니다." }
    }

    fun findSectionById(id: Long): ReviewSection? = sections.find { it.id == id }

    fun findItemById(id: Long): ReviewItem? =
        sections
            .flatMap { it.items }
            .find { it.id == id }

    fun update(type: ReviewType, newSections: List<ReviewSection>) {
        reviewGroup.checkIsPreparing()

        require(newSections.isNotEmpty()) { "섹션은 최소 1개 이상이어야 합니다." }

        this.type = type
        sections.clear()
        sections.addAll(newSections)
    }
}
