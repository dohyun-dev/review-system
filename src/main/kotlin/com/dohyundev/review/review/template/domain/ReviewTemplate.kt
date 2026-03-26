package com.dohyundev.review.review.template.domain

import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class ReviewTemplate(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    @Column(nullable = false)
    var sortOrder: Int,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewTemplate")
    val sections: MutableList<ReviewTemplateSection> = mutableListOf(),
) : BaseAggregateRootEntity<ReviewTemplate>() {

    companion object {
        fun create(name: String, sortOrder: Int, description: String? = null): ReviewTemplate {
            require(name.isNotBlank()) { "템플릿 이름은 공백일 수 없습니다" }
            return ReviewTemplate(name = name, description = description, sortOrder = sortOrder)
        }
    }

    fun update(name: String, description: String?, sortOrder: Int) {
        require(name.isNotBlank()) { "템플릿 이름은 공백일 수 없습니다" }
        this.name = name
        this.description = description
        this.sortOrder = sortOrder
    }

    fun updateForm(sections: List<ReviewTemplateSection>) {
        this.sections.clear()
        this.sections.addAll(sections)
        sections.forEach { it.reviewTemplate = this }
    }

    fun addSection(section: ReviewTemplateSection) {
        section.changeSortOrder((sections.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        sections.add(section)
        section.reviewTemplate = this
    }

    fun removeSection(section: ReviewTemplateSection) {
        sections.remove(section)
        section.reviewTemplate = null
    }
}
