package com.example.model

enum class CountryEdition(val shortName: String, val fullTitle: String, val subheader: String, val tutorName: String, val tutorTitle: String) {
    AMERICAN(
        shortName = "American",
        fullTitle = "DAILY AMERICAN",
        subheader = "Routines, cultural customs & spoken American English",
        tutorName = "Sam",
        tutorTitle = "AMERICAN TUTOR"
    ),
    CANADIAN(
        shortName = "Canadian",
        fullTitle = "DAILY CANADIAN",
        subheader = "Routines, cultural customs & spoken Canadian English",
        tutorName = "Robin",
        tutorTitle = "CANADIAN TUTOR"
    );

    val displayName: String get() = shortName
}

enum class DayCategory(val label: String, val subtitle: String) {
    WEEKDAY("A Typical Weekday", "From morning commute to late-night TV"),
    WEEKEND("A Classic Weekend", "Errands, sports, cookouts & leisure"),
    HOLIDAYS("Traditions & Seasons", "Tailgates, cottage weekends & holidays")
}

enum class CalloutType(val displayBadge: String) {
    SAY_IT_LIKE_A_LOCAL("Say it like a local"),
    CULTURE_NOTE("Culture note")
}

data class TimelineEntry(
    val id: String,
    val time: String,
    val category: DayCategory,
    val title: String,
    val description: String,
    val calloutType: CalloutType,
    val calloutTitle: String,
    val calloutBody: String,
    val idiomSample: String? = null,
    val culturalContextExtra: String = "",
    val practicePrompt: String = "",
    val edition: CountryEdition = CountryEdition.AMERICAN
)
