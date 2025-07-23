package com.mobicom.s16.grp6.choreo

data class Task(
    val id: Int,
    var title: String,
    var description: String,
    var dueDate: String?, // ISO string or Date type
    var priority: Int?,   // e.g. 1-5
    var assignee: String?,
    var isRecurring: Boolean,
    var recurrenceInterval: String? // e.g. "Weekly", "Monthly", or an interval value
)