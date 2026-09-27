package com.example.model

enum class PracticeScenario(
    val title: String,
    val description: String,
    val promptContext: String,
    val starterPromptsAmerican: List<String>,
    val starterPromptsCanadian: List<String>
) {
    CASUAL_CHAT(
        title = "Daily Chit-Chat",
        description = "Small talk, morning routines, weather & plans",
        promptContext = "friendly everyday conversation and casual small talk",
        starterPromptsAmerican = listOf(
            "How was your morning commute today?",
            "What's the weather looking like for the weekend?",
            "Do you usually make breakfast or grab something on the go?"
        ),
        starterPromptsCanadian = listOf(
            "Chilly out there today, eh?",
            "What's your favorite thing about cottage weekends?",
            "Do you usually grab a morning double-double at Timmies?"
        )
    ),
    DRIVE_THRU_DINER(
        title = "Drive-Thru & Diner",
        description = "Ordering iced coffee, custom meals & paying checks",
        promptContext = "ordering food and drinks at a fast-food drive-thru, coffee shop counter, or casual diner",
        starterPromptsAmerican = listOf(
            "Can I get a large iced cold brew with oat milk and a toasted bagel to go?",
            "How do I ask for eggs over-easy and crispy bacon at a diner?",
            "Could we get the check whenever you have a second?"
        ),
        starterPromptsCanadian = listOf(
            "Hi, can I get a medium double-double and an apple fritter, please?",
            "How do Canadians order coffee and breakfast at Tim Hortons?",
            "Can we split the bill on two separate cards, please?"
        )
    ),
    WORKPLACE(
        title = "Office & Business",
        description = "Standups, polite follow-ups & meeting idioms",
        promptContext = "professional office discussions, watercooler banter, and standard business idioms",
        starterPromptsAmerican = listOf(
            "Let's touch base tomorrow morning on the budget deck.",
            "Can you give me a ballpark figure for the project timeline?",
            "How do I politely circle back with someone who hasn't replied to my email?"
        ),
        starterPromptsCanadian = listOf(
            "Let's touch base on the client deliverable by end of day.",
            "How do Canadian workplaces balance friendliness and professionalism?",
            "Could you give me a quick heads up before you submit the report?"
        )
    ),
    ERRANDS_SHOPPING(
        title = "Errands & Shopping",
        description = "Grocery checkout, asking clerks & returning items",
        promptContext = "everyday shopping, supermarket checkouts, asking customer service for help, and returns",
        starterPromptsAmerican = listOf(
            "Excuse me, which aisle are the paper towels and cleaning supplies in?",
            "I'd like to return this shirt; I have the receipt right here.",
            "How should I answer when the cashier asks 'Cash or card?' or 'Cash back?'"
        ),
        starterPromptsCanadian = listOf(
            "Excuse me, do you have change for a toonie for the grocery cart?",
            "I'd like to return this jacket; here is the store receipt.",
            "Do you charge for paper bags, or should I use my reusable totes?"
        )
    ),
    SLANG_AND_IDIOMS(
        title = "Idioms & Local Slang",
        description = "Say it like a native local with cultural context",
        promptContext = "authentic colloquial idioms, local humor, and natural phrasing",
        starterPromptsAmerican = listOf(
            "What does 'touch and go' or 'play it by ear' mean in daily conversation?",
            "Teach me 3 idioms Americans say all the time that textbooks don't teach.",
            "How do I say 'I'm very busy' like an American without sounding rude?"
        ),
        starterPromptsCanadian = listOf(
            "Explain how Canadians naturally use 'eh' without sounding over-the-top.",
            "What do 'toque', 'two-four', 'loonie', and 'keener' mean?",
            "Why do Canadians apologize so much, and how do people respond?"
        )
    );

    fun getPromptsFor(edition: CountryEdition): List<String> {
        return if (edition == CountryEdition.CANADIAN) starterPromptsCanadian else starterPromptsAmerican
    }
}
