package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.common.SortOrderable
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
abstract class ReviewItemOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    label: String,

    description: String? = null,

    override var sortOrder: Int,
) : SortOrderable<ReviewItemOption> {

    @Column(nullable = false, length = 100)
    val label: String = label.also {
        require(it.isNotBlank()) { "라벨은 필수입니다." }
        require(it.length <= 30) { "라벨은 최대 30자까지 입력 가능합니다." }
    }

    @Column(length = 200)
    val description: String? = description?.also {
        require(it.length <= 50) { "설명은 최대 50자까지 입력 가능합니다." }
    }
}