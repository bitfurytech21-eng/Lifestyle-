package com.example.data

import com.example.model.CalloutType
import com.example.model.CountryEdition
import com.example.model.DayCategory
import com.example.model.TimelineEntry

object TimelineRepository {

    val allEntries: List<TimelineEntry> = listOf(
        // ==========================================
        // === AMERICAN EDITION: WEEKDAY ROUTINE ===
        // ==========================================
        TimelineEntry(
            id = "w1",
            edition = CountryEdition.AMERICAN,
            time = "6:30 AM",
            category = DayCategory.WEEKDAY,
            title = "Coffee Run & The Morning Shuffle",
            description = "The alarm goes off. Millions head to the kitchen for a fresh drip brew or pull into a neighborhood drive-thru to grab a large iced coffee before the day kicks into gear.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"To-go cup\" & \"Grab-and-go\"",
            calloutBody = "Americans rarely sit down for morning coffee on weekdays. You'll hear: \"I need a quick grab-and-go breakfast,\" or \"Can I get that in a to-go cup?\"",
            idiomSample = "I'm just gonna swing by the drive-thru for a quick grab-and-go coffee before work.",
            culturalContextExtra = "Drive-thru lanes are a staple of American mornings. Orders are placed through a speaker post and picked up at the window in under two minutes. Huge 24oz or 32oz iced drinks are common year-round, even in freezing winter.",
            practicePrompt = "Can we practice ordering an iced coffee and breakfast sandwich at an American drive-thru?"
        ),
        TimelineEntry(
            id = "w2",
            edition = CountryEdition.AMERICAN,
            time = "7:45 AM",
            category = DayCategory.WEEKDAY,
            title = "Commute & Morning Radio",
            description = "Highways fill bumper-to-bumper with cars, while subway riders in major cities queue up on platforms. Podcasts, NPR news updates, and morning talk shows soundtrack the ride to work.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The Solitary Car Commute",
            calloutBody = "Over 75% of Americans drive alone to work. Traffic radio gives updates on \"rubbernecking\" (slowing down to look at accidents) and \"rush hour gridlock.\"",
            idiomSample = "Traffic on the interstate was bumper-to-bumper; I got stuck in gridlock for forty minutes.",
            culturalContextExtra = "Car culture is deeply ingrained. Many commuters view their 30-to-45-minute solo drive as essential decompression time, listening to audiobooks or calling family on hands-free Bluetooth.",
            practicePrompt = "How do Americans complain about traffic and commuting in casual conversation with coworkers?"
        ),
        TimelineEntry(
            id = "w3",
            edition = CountryEdition.AMERICAN,
            time = "9:00 AM",
            category = DayCategory.WEEKDAY,
            title = "Morning Standup & Water Cooler Chat",
            description = "Offices and remote video calls start with brief friendly pleasantries before diving into tasks. Coworkers exchange weekend recaps around the breakroom kitchen.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Touch base\" & \"Circle back\"",
            calloutBody = "Corporate American English is packed with baseball and navigation metaphors. \"Let's touch base after lunch,\" means let's talk briefly. \"I'll circle back to you,\" means I will follow up later.",
            idiomSample = "Let's touch base on the budget deck tomorrow morning and circle back with the client by Friday.",
            culturalContextExtra = "\"How are you?\" or \"How's it going?\" in an American office is a standard greeting, not an invitation for a detailed medical report. The standard polite reply is \"Good, thanks! How about you?\"",
            practicePrompt = "Can you help me practice common American office idioms like 'touch base', 'ballpark figure', and 'get the ball rolling'?"
        ),
        TimelineEntry(
            id = "w4",
            edition = CountryEdition.AMERICAN,
            time = "12:00 PM",
            category = DayCategory.WEEKDAY,
            title = "Lunch Break: Fast Casual & Brown-Bagging",
            description = "At noon, workers step out for a quick custom grain bowl or unpack a sandwich brought from home. Desk lunches are widespread, keeping the break under thirty minutes.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The 30-Minute Fast-Casual Culture",
            calloutBody = "Chains like Chipotle, Sweetgreen, and Panera thrive on assembly-line customization where you choose greens, grains, proteins, and dressings down the counter line.",
            idiomSample = "I brown-bagged my lunch today, but the team is ordering takeout if you want to jump in on it.",
            culturalContextExtra = "American lunch hours are typically brisk (30 to 45 minutes). Sit-down restaurant lunches on weekdays are generally reserved for special client meetings or Friday celebrations.",
            practicePrompt = "How do I order smoothly at a fast-casual custom bowl restaurant like Chipotle without hesitating?"
        ),
        TimelineEntry(
            id = "w5",
            edition = CountryEdition.AMERICAN,
            time = "3:30 PM",
            category = DayCategory.WEEKDAY,
            title = "School Pickup & Carpool Hustle",
            description = "Flocks of yellow school buses unload in neighborhoods while long lines of minivans and SUVs form in school pickup lanes. Kids head off to soccer practice, band, or tutoring.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Carpooling\" & \"Juggling schedules\"",
            calloutBody = "\"Carpool\" means parents taking turns driving groups of neighborhood kids. Parents often remark: \"We're juggling three different practices this afternoon!\"",
            idiomSample = "I'm on carpool duty for soccer practice today, so I'll be tied up between three and five.",
            culturalContextExtra = "Extracurricular activities are central to American childhood. Suburban parents often spend 4:00 PM to 7:00 PM as informal chauffeurs between dance, gymnastics, scouting, and sports.",
            practicePrompt = "What phrases do American parents use when coordinating carpools and talking about their kids' sports schedules?"
        ),
        TimelineEntry(
            id = "w6",
            edition = CountryEdition.AMERICAN,
            time = "5:15 PM",
            category = DayCategory.WEEKDAY,
            title = "Running Errands & The Grocery Haul",
            description = "On the way home from work, people knock out quick errands: returning Amazon packages, picking up a prescription at the pharmacy drive-thru, or stocking up on groceries.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Running errands\" & \"A quick run\"",
            calloutBody = "\"Running errands\" covers all small chores outside the house. Americans say: \"I need to do a Target run,\" or \"I'm running out to grab milk.\"",
            idiomSample = "I've got a mountain of errands to run before dinner, so I'm making a quick grocery run right now.",
            culturalContextExtra = "Supermarkets in the US are enormous, often carrying thousands of items plus a bakery, deli, pharmacy, and floral department under one roof. Bulk shopping at warehouse clubs is also standard.",
            practicePrompt = "Teach me the most common American shopping and checkout expressions, like 'paper or plastic' and 'cash back'."
        ),
        TimelineEntry(
            id = "w7",
            edition = CountryEdition.AMERICAN,
            time = "6:30 PM",
            category = DayCategory.WEEKDAY,
            title = "Dinner: Home Cooking & Takeout Boxes",
            description = "Families gather around the kitchen island or coffee table. Dinners range from one-pot sheet pan meals and pasta to delivery pizza or tacos on \"Taco Tuesday.\"",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Doggy Bags & Leftover Culture",
            calloutBody = "American restaurant portions are notoriously large, so asking for a \"to-go box\" or \"doggy bag\" to take home leftovers for tomorrow's lunch is universally expected.",
            idiomSample = "These portions are huge—could we get a couple of to-go boxes for the rest of this?",
            culturalContextExtra = "Informality is the norm. Dinners frequently happen around kitchen islands while chatting, or in front of the TV watching sports or news. Tipping 18-22% at full-service restaurants is customary.",
            practicePrompt = "How do I ask for the bill, split a check, and ask for to-go boxes at an American restaurant?"
        ),
        TimelineEntry(
            id = "w8",
            edition = CountryEdition.AMERICAN,
            time = "7:45 PM",
            category = DayCategory.WEEKDAY,
            title = "Evening Walk & Neighborhood Greetings",
            description = "As the sun dips, suburban sidewalks come alive with dog walkers and joggers. Neighbors tending their lawns offer casual waves and brief sidewalk check-ins.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "The Casual American Wave & Nod",
            calloutBody = "Passing someone on a neighborhood walk usually calls for eye contact, a slight smile, a head nod, or a friendly \"Hey, how's it going?\" or \"Have a good one!\"",
            idiomSample = "Saw Mike out walking his golden retriever and we chatted for a second—super nice guy.",
            culturalContextExtra = "American friendliness with strangers on quiet residential streets is genuine, though brief. Saying \"Have a good one!\" is the all-purpose warm American goodbye.",
            practicePrompt = "What are the most natural ways to greet neighbors or people passing by on a sidewalk in the US?"
        ),
        TimelineEntry(
            id = "w9",
            edition = CountryEdition.AMERICAN,
            time = "9:00 PM",
            category = DayCategory.WEEKDAY,
            title = "Streaming on the Couch & Wind-Down",
            description = "The dishwasher hums in the background. With chores wrapped up, people sink into the sofa to binge a new TV series on Netflix, scroll social feeds, and set alarms for tomorrow.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Binge-watching\" & \"Calling it a night\"",
            calloutBody = "\"Binge-watching\" means watching multiple episodes back-to-back. When you are tired and heading to bed, you say: \"Alright, I'm calling it a night.\"",
            idiomSample = "We ended up binge-watching three episodes of that new documentary, but I'm beat so I'm calling it a night.",
            culturalContextExtra = "Discussing favorite TV shows and streaming recommendations is one of the top low-stakes small talk topics across American workplaces.",
            practicePrompt = "How do Americans recommend TV shows, movies, and podcasts to their friends in everyday talk?"
        ),

        // === AMERICAN EDITION: WEEKEND ROUTINE ===
        TimelineEntry(
            id = "e1",
            edition = CountryEdition.AMERICAN,
            time = "8:00 AM",
            category = DayCategory.WEEKEND,
            title = "Saturday Diner Breakfast & Drip Coffee",
            description = "Weekends start at the local diner booth or pancake house. Orders of hash browns, bacon, over-easy eggs, and bottomless mug refills arrive sizzling hot.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Short stack\" & \"Sunny-side up\"",
            calloutBody = "Diner menus have a rich vocabulary: a \"short stack\" is two pancakes (vs a full stack of three or four); \"over-easy\" is runny yolk flipped once; \"sunny-side up\" is unflipped.",
            idiomSample = "I'll take the two-egg breakfast with sunny-side up eggs, crispy hash browns, and a short stack of buttermilk pancakes.",
            culturalContextExtra = "Waitstaff frequently circulate with coffee pots offering \"warm-ups\" or free refills without asking. Leaving cash on the table or paying up front at the register is standard.",
            practicePrompt = "Let's roleplay ordering breakfast at a classic American diner with eggs, toast, and sides."
        ),
        TimelineEntry(
            id = "e2",
            edition = CountryEdition.AMERICAN,
            time = "10:30 AM",
            category = DayCategory.WEEKEND,
            title = "Hardware Store Run & Yard Work",
            description = "Saturday mornings mean a pilgrimage to Home Depot or Lowe's for mulch, lawnmower gas, and DIY project supplies. The smell of cut grass fills suburban air.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Suburban Lawn Care & DIY Pride",
            calloutBody = "Lawn maintenance is a serious suburban hobby. Having a neat, green lawn is seen as neighborly pride. DIY (\"Do-It-Yourself\") home improvement is celebrated.",
            idiomSample = "Gotta spend Saturday tackling yard work—mowing the lawn and edging the driveway before it rains.",
            culturalContextExtra = "Big-box hardware stores are cathedral-sized warehouses where shoppers rent trucks, buy lumber, test power tools, and load 50-lb bags of soil into trunks.",
            practicePrompt = "What vocabulary and idioms do Americans use for home repairs, DIY projects, and yard work?"
        ),
        TimelineEntry(
            id = "e3",
            edition = CountryEdition.AMERICAN,
            time = "1:30 PM",
            category = DayCategory.WEEKEND,
            title = "Youth Sports Sidelines & Lawn Chairs",
            description = "Parks and school fields are covered with folding camp chairs and coolers. Parents cheer on kids playing baseball, softball, soccer, or flag football in the afternoon sun.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Way to go!\" & \"Rooting for\"",
            calloutBody = "American cheering phrases are upbeat: \"Way to hustle!\", \"Good eye!\", and \"We're rooting for you!\" Rooting means enthusiastically supporting a team or person.",
            idiomSample = "We're all out here rooting for the Wildcats—way to hustle out there!",
            culturalContextExtra = "Youth sports culture is a massive weekend community gathering. Parents bring foldable pop-up canopies, Yeti thermoses, and provide halftime orange slices.",
            practicePrompt = "How do Americans talk about sports, support their favorite teams, and use sports metaphors in everyday life?"
        ),
        TimelineEntry(
            id = "e4",
            edition = CountryEdition.AMERICAN,
            time = "5:00 PM",
            category = DayCategory.WEEKEND,
            title = "Backyard Cookout & Lawn Games",
            description = "Charcoal grills smoke with burgers, hot dogs, and corn on the cob. Friends sip cold drinks while playing cornhole, kanjam, or horseshoes on the grass.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"BYOB\" & \"Potluck\"",
            calloutBody = "\"BYOB\" stands for Bring Your Own Beverage/Beer. A \"potluck\" is a gathering where every guest brings a dish, salad, or dessert to share.",
            idiomSample = "The cookout is BYOB, but we've got burgers and dogs on the grill, so just bring a side or dessert.",
            culturalContextExtra = "Host etiquette is relaxed: guests usually walk straight to the backyard via the side gate. Asking \"What can I bring?\" when invited is good manners.",
            practicePrompt = "Can we practice casual social conversation when attending an American backyard barbecue?"
        ),
        TimelineEntry(
            id = "e5",
            edition = CountryEdition.AMERICAN,
            time = "8:30 PM",
            category = DayCategory.WEEKEND,
            title = "Fire Pit & S'mores Under the Stars",
            description = "As temperatures cool, neighbors gather around backyard fire pits. Marshmallows are toasted on sticks, sandwiched between graham crackers and Hershey's chocolate bars.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The American S'more Tradition",
            calloutBody = "The name comes from a contraction of \"some more.\" Making s'mores is a quintessential American summer and autumn ritual for kids and adults alike.",
            idiomSample = "Let's get the fire pit going and roast some marshmallows for s'mores.",
            culturalContextExtra = "Porch and patio living is a hallmark of American homes. String lights, Adirondack chairs, and fire pits turn backyards into outdoor living rooms.",
            practicePrompt = "What are common ways to talk about camping, bonfires, and outdoor weekend getaways in American English?"
        ),

        // === AMERICAN EDITION: TRADITIONS & SEASONS ===
        TimelineEntry(
            id = "h1",
            edition = CountryEdition.AMERICAN,
            time = "9:00 AM",
            category = DayCategory.HOLIDAYS,
            title = "Thanksgiving Morning Turkey Trot 5K",
            description = "Before feasting on roast turkey, stuffing, and pumpkin pie, hundreds of thousands of people across town run or walk a brisk 5-kilometer morning race in silly turkey hats.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The \"Turkey Trot\" Phenomenon",
            calloutBody = "Thanksgiving is the most popular running day of the year in the US. Running a morning 5K is jokingly called \"earning your pie.\"",
            idiomSample = "The whole family signed up for the Turkey Trot 5K to earn our second helpings of pumpkin pie.",
            culturalContextExtra = "Thanksgiving is the quintessential American family holiday, centered around gratitude, giant home-cooked meals, the Macy's Thanksgiving Day Parade, and NFL football on TV.",
            practicePrompt = "How do Americans describe Thanksgiving traditions, foods like cranberry sauce and stuffing, and family gatherings?"
        ),
        TimelineEntry(
            id = "h2",
            edition = CountryEdition.AMERICAN,
            time = "1:00 PM",
            category = DayCategory.HOLIDAYS,
            title = "Game Day Tailgating & Stadium Fever",
            description = "Stadium parking lots transform into giant outdoor street festivals hours before kickoff. Truck beds open up with TV screens, portable smokers, and team banners.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Tailgating\" & \"Game day gear\"",
            calloutBody = "\"Tailgating\" gets its name from folding down the tailgate of a pickup truck to serve food and drinks before a sporting event or concert.",
            idiomSample = "We set up our tailgate in lot 4 around ten AM with breakfast burritos and cornhole.",
            culturalContextExtra = "College and professional football tailgating is an American cultural institution with elaborate setups, school fight songs, and regional culinary traditions.",
            practicePrompt = "Can we practice game day banter and talk about sports fan culture in the USA?"
        ),
        TimelineEntry(
            id = "h3",
            edition = CountryEdition.AMERICAN,
            time = "6:00 PM",
            category = DayCategory.HOLIDAYS,
            title = "Fourth of July Neighborhood Block Party",
            description = "Streets close to traffic as neighbors set up folding tables end-to-end. Kids decorate bicycles with red, white, and blue streamers before municipal fireworks light up the night sky.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The Classic Summer Block Party",
            calloutBody = "Independence Day block parties bring together entire streets for watermelon-eating contests, parades, sparklers, and dusk fireworks displays.",
            idiomSample = "The street is blocked off for the Fourth of July block party—don't forget to grab your lawn chairs for the fireworks.",
            culturalContextExtra = "The 4th of July is celebrated outdoors with sparklers, red-white-and-blue clothing, hot dogs, and free public fireworks shows in town parks and waterfronts.",
            practicePrompt = "How do Americans talk about summer holidays, fireworks, and block parties?"
        ),

        // ==========================================
        // === CANADIAN EDITION: WEEKDAY ROUTINE ===
        // ==========================================
        TimelineEntry(
            id = "cw1",
            edition = CountryEdition.CANADIAN,
            time = "6:45 AM",
            category = DayCategory.WEEKDAY,
            title = "Tim Hortons Drive-Thru & Morning Shovel",
            description = "Before heading out into brisk morning air, drivers scrape the frost off windshields or dig out the driveway. A quick turn into 'Timmies' provides the fuel for the morning commute.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Double-Double\" & \"Timbits\"",
            calloutBody = "In Canada, coffee shorthand is legendary: a \"double-double\" means two creams, two sugars. \"Timbits\" are bite-sized donut holes ordered in boxes of 10 or 20.",
            idiomSample = "Can I get a large double-double and a ten-pack of assorted Timbits, please?",
            culturalContextExtra = "Tim Hortons holds a unique spot in Canadian national identity. Even in sub-zero January weather, you'll see Canadians ordering large iced capps while wearing winter toques.",
            practicePrompt = "Let's roleplay ordering at a Tim Hortons drive-thru like a true Canadian local!"
        ),
        TimelineEntry(
            id = "cw2",
            edition = CountryEdition.CANADIAN,
            time = "8:00 AM",
            category = DayCategory.WEEKDAY,
            title = "Subway Rush & The Canadian Apology Cadence",
            description = "Commuters file onto the TTC in Toronto, the STM in Montreal, or the SkyTrain in Vancouver. Bumped shoulders on crowded platforms are instantly met with instinctive polite apologies.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The Multi-Purpose \"Sorry\"",
            calloutBody = "Canadians use \"sorry\" (pronounced 'sore-ee') not just to admit fault, but as an all-purpose polite buffer for excuse-me, pardon, or empathy. Canada even has an 'Apology Act' legally establishing that saying sorry isn't an admission of guilt!",
            idiomSample = "Oh sorry, didn't see you there—go right ahead, eh!",
            culturalContextExtra = "Transit etiquette is polite and quiet. Riders queue neatly to the right on escalators and remove heavy backpacks on packed train cars.",
            practicePrompt = "How do Canadians use 'sorry', 'pardon', and polite conversational padding in public situations?"
        ),
        TimelineEntry(
            id = "cw3",
            edition = CountryEdition.CANADIAN,
            time = "9:15 AM",
            category = DayCategory.WEEKDAY,
            title = "Office Check-In & The Unspoken 'Eh'",
            description = "Work begins with weather updates and gentle banter. Coworkers grab tea in mugs and discuss the previous night's NHL hockey game around the office kitchenette.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Eh?\" & Canadian Raising",
            calloutBody = "\"Eh\" is used naturally at the end of sentences to invite agreement (like \"right?\" or \"don't you think?\"). Example: \"Cold out there today, eh?\" Canadian Raising also gives words like 'about' a distinct, crisp diphthong.",
            idiomSample = "That presentation went really well, eh? Let's touch base after lunch to finalize the slide deck.",
            culturalContextExtra = "Canadian workplace culture balances American efficiency with British understated modesty. Bragging is discouraged; teamwork and consensus are prized.",
            practicePrompt = "Can we practice using 'eh' and natural Canadian conversational cadence without overdoing it?"
        ),
        TimelineEntry(
            id = "cw4",
            edition = CountryEdition.CANADIAN,
            time = "12:15 PM",
            category = DayCategory.WEEKDAY,
            title = "Lunch Break: PATH Walk & Poutine Cravings",
            description = "In winter, downtown office workers head into underground tunnel networks like Toronto's PATH or Montreal's Underground City to grab gourmet wraps, sushi, or a warm box of gravy-topped poutine.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Poutine & Underground Cities",
            calloutBody = "Originating in Quebec, poutine consists of crispy french fries, fresh squeaky cheese curds, and rich hot brown gravy. Underground PATH pedestrian walkways span tens of kilometers so workers never need a coat between meetings.",
            idiomSample = "Let's walk through the PATH and grab a hot poutine for lunch—it's freezing outside today.",
            culturalContextExtra = "Cheese curds in good poutine must be fresh and 'squeak' when you bite into them. Melted shredded cheddar is considered a poutine sin across Canada.",
            practicePrompt = "How do I order authentic poutine and describe Canadian winter comfort foods in conversation?"
        ),
        TimelineEntry(
            id = "cw5",
            edition = CountryEdition.CANADIAN,
            time = "3:45 PM",
            category = DayCategory.WEEKDAY,
            title = "School Bell & Arena Practice Drop-Off",
            description = "Kids pack their backpacks in knitted toques and winter parkas. Minivans fill the parking lots of local community community centres and hockey arenas for afternoon skating lessons.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Toque\" & \"Runner\"",
            calloutBody = "A \"toque\" (rhymes with duke) is a knit winter beanie. \"Runners\" or \"sneakers\" refer to running shoes. Canadian parents say: \"Grab your toque and pack your runners!\"",
            idiomSample = "Don't forget your toque and mitts on your way out to the rink!",
            culturalContextExtra = "Municipal community arenas are the social heart of Canadian towns. Parents gather along the rink glass drinking thermoses of hot coffee while watching youth figure skating or minor hockey.",
            practicePrompt = "What everyday vocabulary is uniquely Canadian, like 'toque', 'runners', 'washroom', and 'pencil crayons'?"
        ),
        TimelineEntry(
            id = "cw6",
            edition = CountryEdition.CANADIAN,
            time = "5:30 PM",
            category = DayCategory.WEEKDAY,
            title = "Grocery Run: The Two-Dollar 'Toonie' & Milk Bags",
            description = "Picking up dinner ingredients at No Frills or Loblaws. Shoppers in Eastern Canada grab 4-liter plastic pouches of milk to insert into reusable plastic pitchers at home.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Loonie\" & \"Toonie\"",
            calloutBody = "Canada's one-dollar coin features a common loon bird, nicknamed the \"loonie.\" When the two-dollar bi-metallic coin came out, Canadians naturally christened it the \"toonie!\"",
            idiomSample = "I've got a couple of toonies in my jacket pocket for the grocery cart deposit.",
            culturalContextExtra = "Bagged milk in Ontario, Quebec, and the Maritimes is sold in 3-bag outer sacks totaling 4 liters. You snip the corner tip off the bag after placing it in a milk jug.",
            practicePrompt = "Teach me Canadian money terms like 'loonie', 'toonie', 'nickel', and how to pay at a checkout counter."
        ),
        TimelineEntry(
            id = "cw7",
            edition = CountryEdition.CANADIAN,
            time = "7:00 PM",
            category = DayCategory.WEEKDAY,
            title = "Hockey Night & The Family Living Room",
            description = "The hum of the television fills the room with the familiar horn and play-by-play commentary. Dinner plates are cleared and families settle in for regional NHL match-ups.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Hockey as a Cultural Religion",
            calloutBody = "Hockey is far more than a sport in Canada—it is a shared cultural touchstone spanning generations, provinces, and winter nights from St. John's to Vancouver.",
            idiomSample = "The Leafs are playing Montreal tonight—puck drop is at seven sharp, so don't miss the first period!",
            culturalContextExtra = "Game terminology permeates daily life: \"Dropping the gloves\" means confronting an issue directly; \"Skating on thin ice\" means taking a big risk.",
            practicePrompt = "How can I talk about hockey scores, penalties, and NHL games in casual Canadian conversation?"
        ),

        // === CANADIAN EDITION: WEEKEND & COTTAGE ===
        TimelineEntry(
            id = "ce1",
            edition = CountryEdition.CANADIAN,
            time = "8:30 AM",
            category = DayCategory.WEEKEND,
            title = "Weekend Diner & Peameal Bacon Buns",
            description = "Saturday mornings at the local market or neighborhood grill. Thick slices of cornmeal-crusted Canadian peameal bacon sizzle on the flattop grill and are served on soft Kaiser rolls with maple syrup.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Peameal bacon\" & \"Pure Maple Syrup\"",
            calloutBody = "True Canadian bacon is \"peameal bacon\"—cured lean pork loin rolled in ground cornmeal. Real Canadian table etiquette strictly calls for 100% pure Grade A maple syrup, never flavored corn syrup.",
            idiomSample = "I'll grab a peameal bacon sandwich on a bun with hot mustard and a coffee to go.",
            culturalContextExtra = "Quebec produces over 70% of the world's maple syrup. Visiting a traditional rustic \"Sugar Shack\" (cabane à sucre) in spring to eat hot maple taffy poured onto fresh snow is a beloved ritual.",
            practicePrompt = "Let's practice ordering Canadian breakfast specialties and discussing maple syrup traditions!"
        ),
        TimelineEntry(
            id = "ce2",
            edition = CountryEdition.CANADIAN,
            time = "11:00 AM",
            category = DayCategory.WEEKEND,
            title = "Heading \"Up North\" to the Cottage",
            description = "Trunks and roof racks packed with coolers, firewood, and hiking boots as city dwellers make the weekend exodus along highways toward lakes, pines, and cabins.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Cottage\" vs \"Cabin\" vs \"Camp\"",
            calloutBody = "What you call a lakeside retreat depends on geography: In Ontario it's \"the cottage\" (heading 'up north'); in Western Canada it's \"the cabin\"; in Northern Ontario and parts of the Maritimes it's \"the camp.\"",
            idiomSample = "We're packing up the SUV to head up to the cottage for the long weekend.",
            culturalContextExtra = "Cottage weekends mean jumping off wooden docks into freshwater lakes, paddling red cedar canoes, and swatting mosquitoes around outdoor campfires.",
            practicePrompt = "How do Canadians talk about cottage trips, outdoor camping, and summer long weekends?"
        ),
        TimelineEntry(
            id = "ce3",
            edition = CountryEdition.CANADIAN,
            time = "3:30 PM",
            category = DayCategory.WEEKEND,
            title = "Pond Hockey & Frozen Lake Shinny",
            description = "When local ponds freeze thick, boots mark the goal posts and snow is shoveled clear. Kids and adults lace up skates for friendly pickup hockey games without formal referees.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"Shinny\" & Outdoor Rinks",
            calloutBody = "\"Shinny\" is informal pickup pond hockey played with no pads, low pucks, and minimal body contact. Just sticks, skates, and friendly camaraderie on the ice.",
            idiomSample = "Grab your stick and gloves—the neighbors are playing a game of shinny on the outdoor pond!",
            culturalContextExtra = "Many Canadian backyards are converted into makeshift frozen ice rinks with timber boards and garden hoses every winter.",
            practicePrompt = "Can we practice casual Canadian winter sports banter and outdoor recreation talk?"
        ),
        TimelineEntry(
            id = "ce4",
            edition = CountryEdition.CANADIAN,
            time = "6:00 PM",
            category = DayCategory.WEEKEND,
            title = "A Caesar Cocktail & Backyard Smoker",
            description = "Friends gather on the wooden deck for Canada's signature national cocktail: vodka, Clamato juice, Worcestershire sauce, hot sauce, served in a celery-salt rimmed glass garnished with pickled asparagus.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "The Famous Canadian Caesar",
            calloutBody = "Invented in Calgary in 1969, the Caesar is Canada's definitive savory cocktail. Over 400 million Caesars are enjoyed in Canada every single year.",
            idiomSample = "Let's mix up a round of Caesars on the deck while the salmon finishes smoking.",
            culturalContextExtra = "While Americans drink Bloody Marys with tomato juice, Canadians almost exclusively drink Caesars with Clamato (clam broth & tomato juice blend).",
            practicePrompt = "How do Canadians host weekend social drinks and talk about Canadian recipes like butter tarts and Nanaimo bars?"
        ),

        // === CANADIAN EDITION: TRADITIONS & HOLIDAYS ===
        TimelineEntry(
            id = "ch1",
            edition = CountryEdition.CANADIAN,
            time = "12:00 PM",
            category = DayCategory.HOLIDAYS,
            title = "Canada Day Waterfront Picnics & Red-White Gear",
            description = "On July 1st, streets and parks nationwide erupt in maple leaf red and white. Concerts in Ottawa on Parliament Hill and waterfront fireworks celebrate the nation's birthday.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Canada Day Traditions",
            calloutBody = "Canada Day celebrates the 1867 confederation. In Quebec, July 1st is also famously \"Moving Day,\" when tens of thousands of residential leases turn over simultaneously.",
            idiomSample = "Happy Canada Day! We're heading down to the waterfront park to watch the fireworks at dusk.",
            culturalContextExtra = "Parades, pancake breakfasts, indigenous cultural festivals, and spectacular evening fireworks over lakes and oceans mark the celebration.",
            practicePrompt = "How do Canadians celebrate July 1st and express national pride and multicultural unity?"
        ),
        TimelineEntry(
            id = "ch2",
            edition = CountryEdition.CANADIAN,
            time = "2:00 PM",
            category = DayCategory.HOLIDAYS,
            title = "The \"May Two-Four\" Victoria Day Kickoff",
            description = "Victoria Day in late May is the unofficial kickoff to Canadian summer. The long weekend is affectionately dubbed the 'May Two-Four' weekend—referencing both Queen Victoria's May 24th birthday and a 24-can case of beer.",
            calloutType = CalloutType.SAY_IT_LIKE_A_LOCAL,
            calloutTitle = "\"May Two-Four\" & \"A Two-Four\"",
            calloutBody = "In Canada, a 24-can case of beer is universally known as a \"two-four.\" The holiday is synonymous with opening the cottage for the warm season.",
            idiomSample = "We're picking up a two-four of cider and heading up to open the cottage for May Two-Four weekend!",
            culturalContextExtra = "It marks the start of warm weather, garden planting, dock repairs, and outdoor barbecues after long sub-zero Canadian winters.",
            practicePrompt = "Can we practice everyday Canadian holiday slang and long weekend travel phrases?"
        ),
        TimelineEntry(
            id = "ch3",
            edition = CountryEdition.CANADIAN,
            time = "5:30 PM",
            category = DayCategory.HOLIDAYS,
            title = "Canadian Thanksgiving in Crisp October",
            description = "Celebrated on the second Monday of October (six weeks before US Thanksgiving), families gather amid vibrant autumn foliage for roast turkey, tourtière meat pie, and pumpkin tarts.",
            calloutType = CalloutType.CULTURE_NOTE,
            calloutTitle = "Autumn Thanksgiving & Tourtière",
            calloutBody = "Canadian Thanksgiving coincides with harvest festival time in October. In French Canada, traditional spiced pork-and-beef 'tourtière' pie is served alongside roast turkey.",
            idiomSample = "We're hosting Canadian Thanksgiving dinner with the whole family this October long weekend.",
            culturalContextExtra = "Because Canadian Thanksgiving falls in early autumn, it often features scenic foliage hikes, apple orchard picking, and crisp sweater weather.",
            practicePrompt = "How does Canadian Thanksgiving in October compare with American Thanksgiving, and what are common table conversations?"
        )
    )

    fun getEntriesFor(edition: CountryEdition, category: DayCategory): List<TimelineEntry> {
        return allEntries.filter { it.edition == edition && it.category == category }
    }

    fun getEntriesForCategory(category: DayCategory): List<TimelineEntry> {
        return allEntries.filter { it.category == category }
    }
}
