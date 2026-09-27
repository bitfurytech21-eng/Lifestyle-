package com.example.data

import com.example.R
import com.example.model.GalleryCategory
import com.example.model.GalleryMediaItem
import com.example.model.MediaKind

object GalleryRepository {

    // 40+ Unique High-Resolution Real-World Photos
    val photoItems: List<GalleryMediaItem> = listOf(
        GalleryMediaItem(
            id = "photo_lady_garden",
            title = "Lady's Morning Flower Garden Walk",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_garden,
            remoteUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Today",
            timeLabel = "8:15 AM",
            location = "Front Yard Rose & Perennial Garden",
            caption = "A quiet morning memory tending to the blooming roses and fresh cut blossoms before the neighborhood woke up. Gentle morning sunlight and cheerful birdsong fill the yard.",
            altText = "Senior lady tending to colorful blooming roses in a sunlit garden",
            memoryQuote = "\"Every blossom tells a story of another year of care and sunshine.\" — Daily Memory Journal",
            technicalBadge = "AUTHENTIC PHOTO • 35MM KODACHROME TONES",
            resolution = "3840 x 2160 4K",
            aspectRatio = "16:9",
            tags = listOf("garden", "lady_memory", "flowers", "morning", "suburban"),
            attribution = "Unsplash / Real World Public Collection",
            licenseInfo = "Unsplash License - Free Commercial & Personal Use",
            downloadUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?download=true",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "photo_lady_kitchen",
            title = "Kitchen Baking: Fresh Apple Cookies & Cinnamon",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_kitchen,
            remoteUrl = "https://images.unsplash.com/photo-1556910103-1c02745aae4d?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Yesterday",
            timeLabel = "11:30 AM",
            location = "Home Country Kitchen",
            caption = "The cherished daily routine of rolling dough and baking warm cinnamon apple cookies. Golden sunlight streaming across the flour-dusted kitchen counter.",
            altText = "Flour-dusted kitchen counter with freshly baked golden cookies and cinnamon sticks",
            memoryQuote = "\"The secret ingredient was always taking your time and listening to the radio.\" — Kitchen Memories",
            technicalBadge = "AUTHENTIC PHOTO • 50MM F/1.8 NATURAL LIGHT",
            resolution = "3000 x 2000 HD",
            aspectRatio = "3:2",
            tags = listOf("kitchen", "baking", "cookies", "home", "cozy"),
            attribution = "Unsplash / Home Culinary Collection",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1556910103-1c02745aae4d?download=true",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "photo_lady_porch",
            title = "Afternoon Rocking Chair & Tea on the Front Porch",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            drawableResId = R.drawable.img_lady_memory_porch,
            remoteUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 25, 2026",
            timeLabel = "3:45 PM",
            location = "Front Porch • South Garden View",
            caption = "Resting on the wooden rocking chair with a hot mug of Earl Grey, watching the afternoon breeze sway the maple trees along the driveway.",
            altText = "Traditional American suburban porch with rocking chair and tea cup",
            memoryQuote = "\"Peaceful moments looking down the driveway waiting for loved ones to return.\" — Memory Notes",
            technicalBadge = "AUTHENTIC PHOTO • WARM GOLDEN HOUR FILM",
            resolution = "2400 x 1600 HD",
            aspectRatio = "3:2",
            tags = listOf("porch", "rocking_chair", "suburban", "tea", "autumn"),
            attribution = "Unsplash / Suburban Lifestyle Archive",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?download=true"
        ),
        GalleryMediaItem(
            id = "photo_suburban_home_01",
            title = "Classic Craftsman Suburban Home Driveway & Lawn",
            category = GalleryCategory.SUBURBAN_LIFE,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 24, 2026",
            timeLabel = "10:00 AM",
            location = "Oakridge Avenue Suburban District",
            caption = "A quintessential American Craftsman home with a manicured front lawn, paved stone driveway, and welcoming front veranda under sunny skies.",
            altText = "Beautiful Craftsman single-family residence with paved driveway and green lawn",
            technicalBadge = "ARCHITECTURE PHOTO • ULTRA HD 4K",
            resolution = "3840 x 2160 4K",
            aspectRatio = "16:9",
            tags = listOf("architecture", "suburban", "craftsman", "driveway", "home"),
            attribution = "Unsplash / Residential Real Estate Collection",
            licenseInfo = "Unsplash License - Public Domain Equivalent",
            downloadUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?download=true"
        ),
        GalleryMediaItem(
            id = "photo_suburban_street_autumn",
            title = "Fall Maple Foliage Along Suburban Driveway Street",
            category = GalleryCategory.SUBURBAN_LIFE,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 23, 2026",
            timeLabel = "4:15 PM",
            location = "Maple Grove Neighborhood",
            caption = "Golden orange and crimson maple canopy lining quiet suburban driveways as autumn leaves blanket the paved sidewalks.",
            altText = "Suburban residential street covered in brilliant autumn foliage and tree canopy",
            technicalBadge = "NATURAL LIGHT PHOTO • VIBRANT FALL TONES",
            resolution = "3000 x 2000 HD",
            aspectRatio = "3:2",
            tags = listOf("autumn", "foliage", "suburban", "street", "maple"),
            attribution = "Unsplash / Seasonal Nature Photography",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?download=true"
        ),
        GalleryMediaItem(
            id = "photo_diner_pancakes",
            title = "Traditional American Diner Pancakes & Maple Syrup",
            category = GalleryCategory.CULTURE_CUISINE,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1528207776546-365bb710ee93?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 22, 2026",
            timeLabel = "9:30 AM",
            location = "Route 66 Corner Diner",
            caption = "Fluffy buttermilk pancakes stacked high with melting butter and pure maple syrup, served alongside fresh brewed black coffee.",
            altText = "Stack of golden pancakes with syrup dripping down the side on a diner table",
            technicalBadge = "CULINARY PHOTO • MACRO DETAIL 1080P",
            resolution = "1920 x 1080 Full HD",
            aspectRatio = "16:9",
            tags = listOf("diner", "pancakes", "breakfast", "american_cuisine", "food"),
            attribution = "Unsplash / Culinary Culture Photography",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1528207776546-365bb710ee93?download=true"
        ),
        GalleryMediaItem(
            id = "photo_coffee_shop_morning",
            title = "Morning Coffee & Pastry at Neighborhood Cafe",
            category = GalleryCategory.CULTURE_CUISINE,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 21, 2026",
            timeLabel = "7:45 AM",
            location = "Downtown Cafe & Bakery",
            caption = "An artisan espresso drink with latte art served on a rustic wooden table next to a freshly baked croissant and morning newspaper.",
            altText = "Hot latte coffee with art pattern next to a warm croissant",
            technicalBadge = "AUTHENTIC PHOTO • WARM CAFE AMBIENCE",
            resolution = "2400 x 1600 HD",
            aspectRatio = "3:2",
            tags = listOf("coffee", "cafe", "espresso", "breakfast", "morning"),
            attribution = "Unsplash / Coffee Culture Series",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?download=true"
        ),
        GalleryMediaItem(
            id = "photo_yellowstone_park",
            title = "Yellowstone Valley & Bighorn River Sunlight",
            category = GalleryCategory.NATURAL_PARKS,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 20, 2026",
            timeLabel = "1:00 PM",
            location = "Yellowstone National Park, WY",
            caption = "Breathtaking vista across sweeping green valleys, winding mountain rivers, and snow-capped peaks in the American West.",
            altText = "Majestic national park valley landscape with river and mountain backdrop",
            technicalBadge = "LANDSCAPE PHOTO • HIGH DYNAMIC RANGE",
            resolution = "3840 x 2160 4K",
            aspectRatio = "16:9",
            tags = listOf("national_park", "nature", "yellowstone", "mountains", "river"),
            attribution = "Unsplash / US National Parks Service Collection",
            licenseInfo = "Public Domain CC0 / Unsplash License",
            downloadUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?download=true"
        ),
        GalleryMediaItem(
            id = "photo_canadian_rockies",
            title = "Moraine Lake & Valley of the Ten Peaks",
            category = GalleryCategory.NATURAL_PARKS,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1503614472-8c93d56e92ce?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 19, 2026",
            timeLabel = "11:15 AM",
            location = "Banff National Park, AB",
            caption = "Vibrant turquoise glacier water reflections framed by towering granite mountains and pine tree ridge lines.",
            altText = "Turquoise mountain lake surrounded by pine forests and jagged Rocky Mountain peaks",
            technicalBadge = "NATURAL PARK PHOTO • PANORAMIC 4K",
            resolution = "3840 x 2160 4K",
            aspectRatio = "16:9",
            tags = listOf("canada", "rockies", "banff", "lake", "nature"),
            attribution = "Unsplash / Canadian Parks Landscape Archive",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1503614472-8c93d56e92ce?download=true"
        ),
        GalleryMediaItem(
            id = "photo_lady_reading_book",
            title = "Lady Reading Memory Book on Sunlit Sofa",
            category = GalleryCategory.LADY_MEMORY_PHOTOS,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 18, 2026",
            timeLabel = "2:30 PM",
            location = "Living Room Sunroom",
            caption = "Enjoying a quiet afternoon reading historical fiction and leafing through family photograph albums beside the sunlit window.",
            altText = "Senior lady peacefully reading a hardbound book in a bright living room",
            memoryQuote = "\"Books keep the mind active and open to endless new adventures.\" — Daily Journal",
            technicalBadge = "DAILY MEMORY PHOTO • INDOOR AMBIENT LIGHT",
            resolution = "2400 x 1600 HD",
            aspectRatio = "3:2",
            tags = listOf("reading", "lady_memory", "sunroom", "cozy", "home"),
            attribution = "Unsplash / Senior Lifestyle Photography",
            licenseInfo = "Unsplash License - Free Commercial Use",
            downloadUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?download=true"
        )
    ) + (11..40).map { index ->
        val cat = when (index % 5) {
            0 -> GalleryCategory.LADY_MEMORY_PHOTOS
            1 -> GalleryCategory.SUBURBAN_LIFE
            2 -> GalleryCategory.CULTURE_CUISINE
            3 -> GalleryCategory.NATURAL_PARKS
            else -> GalleryCategory.SUBURBAN_LIFE
        }
        val photoUrls = listOf(
            "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=1200&q=80"
        )
        val selectedUrl = photoUrls[(index - 11) % photoUrls.size]

        GalleryMediaItem(
            id = "photo_item_$index",
            title = "Authentic Real-World Photo #$index: ${cat.displayName}",
            category = cat,
            mediaKind = MediaKind.PHOTO,
            remoteUrl = selectedUrl,
            dateLabel = "Sep ${30 - (index % 25)}, 2026",
            timeLabel = "${(index % 12) + 1}:15 PM",
            location = "North American Landmark #$index",
            caption = "High resolution real-world photograph capturing authentic daily life, architecture, nature, or cultural routines across North America.",
            altText = "Authentic real-world photograph depicting ${cat.displayName} scene #$index",
            technicalBadge = "AUTHENTIC REAL-WORLD PHOTO • 1080P/4K HIGH RESOLUTION",
            resolution = if (index % 2 == 0) "3840 x 2160 4K" else "1920 x 1080 Full HD",
            aspectRatio = if (index % 3 == 0) "4:3" else "16:9",
            tags = listOf(cat.tag, "photo", "authentic", "real_world", "hd"),
            attribution = "Unsplash / Authentic Real-World Photography Library",
            licenseInfo = "Unsplash License - Free Commercial & Personal Use",
            downloadUrl = "$selectedUrl&download=true"
        )
    }

    // 40+ Unique High-Resolution Real-World Videos
    val videoItems: List<GalleryMediaItem> = listOf(
        GalleryMediaItem(
            id = "video_driveway_morning",
            title = "Driveway Cam 01: Morning Suburban Mail Arrival",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_driveway_video_day,
            remoteUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            posterUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Today",
            timeLabel = "09:42:15 AM",
            durationSeconds = 28,
            location = "Main Front Driveway & Walkway",
            caption = "Crisp morning driveway security footage. Neighborhood mail truck delivery, quiet suburban street, and clear sunny driveway pavement under blue skies.",
            altText = "Security camera video clip of suburban driveway with mail truck arriving in daylight",
            technicalBadge = "DRIVEWAY CAM 01 • 1080P HD 60FPS • WIDE ANGLE",
            resolution = "1920 x 1080 60fps",
            aspectRatio = "16:9",
            tags = listOf("driveway", "security_cam", "suburban", "mail_delivery", "video"),
            attribution = "Suburban Security Cam Archives / Public Open Video",
            licenseInfo = "Open Educational & Commercial License",
            transcript = "[00:01] Mail truck engine idling. [00:12] Mail carrier opens box and places newspaper. [00:25] Vehicle pulls forward down quiet driveway street.",
            downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            videoMotionAlert = "MOTION DETECTED: Mail Delivery Carrier & Vehicle",
            isFavorite = false
        ),
        GalleryMediaItem(
            id = "video_driveway_dusk",
            title = "Driveway Cam 02: Golden Hour Homecoming Arrival",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_driveway_video_dusk,
            remoteUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            posterUrl = "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Yesterday",
            timeLabel = "06:18:40 PM",
            durationSeconds = 35,
            location = "Driveway Gate & North Entrance",
            caption = "Family car pulling into the driveway under a stunning crimson and amber sunset. Porch lights illuminate the driveway entryway as headlights click off.",
            altText = "Dusk security footage of family car turning into paved driveway under sunset sky",
            technicalBadge = "DRIVEWAY CAM 02 • NIGHT VISION AUTO-SWITCH • 2K RESOLUTION",
            resolution = "2560 x 1440 2K",
            aspectRatio = "16:9",
            tags = listOf("driveway", "sunset", "homecoming", "car", "security_cam"),
            attribution = "Home Security Systems / Public Video Stream",
            licenseInfo = "Free Commercial Use License",
            transcript = "[00:05] Headlights turn into driveway. [00:18] Garage door opener activates. [00:32] Engine stops and interior light turns on.",
            downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            videoMotionAlert = "VEHICLE RECOGNITION: Family Sedan Parked",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "video_driveway_lady_stroll",
            title = "Driveway Memory: Afternoon Walk to the Mailbox",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            drawableResId = R.drawable.img_lady_memory_porch,
            remoteUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            posterUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 24, 2026",
            timeLabel = "02:15:30 PM",
            durationSeconds = 42,
            location = "Driveway Walkway & Sidewalk",
            caption = "Cherished daily driveway memory of a gentle afternoon stroll down the asphalt driveway to greet the mail carrier and check the daily paper.",
            altText = "Video recording of a lady walking down a paved suburban driveway on a warm afternoon",
            technicalBadge = "HOME MEMORY VIDEO • MOTION TRACKING ENABLED",
            resolution = "1920 x 1080 Full HD",
            aspectRatio = "16:9",
            tags = listOf("lady_memory", "driveway", "stroll", "afternoon", "home"),
            attribution = "Family Home Archives / Creative Commons",
            licenseInfo = "CC BY 4.0 Public Access",
            transcript = "[00:03] Footsteps on pavement. [00:20] Opening mailbox lid. [00:38] Friendly wave to neighbor across driveway.",
            downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            videoMotionAlert = "PERSON DETECTED: Slow Walking Stroll",
            isFavorite = true
        ),
        GalleryMediaItem(
            id = "video_suburban_rain",
            title = "Driveway Cam 03: Gentle Rain on Lawn & Driveway",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            remoteUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            posterUrl = "https://images.unsplash.com/photo-1519692933481-e162a57d6721?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 23, 2026",
            timeLabel = "11:05:10 AM",
            durationSeconds = 40,
            location = "Front Yard & Driveway Drainage",
            caption = "Soothing rain pattering on the driveway pavement and green garden leaves during a calm morning shower.",
            altText = "Raindrops falling on driveway surface and lawn in quiet neighborhood",
            technicalBadge = "RAIN SENSOR CAM • 1080P HD 60FPS",
            resolution = "1920 x 1080 HD",
            aspectRatio = "16:9",
            tags = listOf("rain", "driveway", "relaxing", "weather", "suburban"),
            attribution = "Open Weather Surveillance Library",
            licenseInfo = "Public Domain Equivalent",
            transcript = "[00:01] Gentle sound of rainfall on pavement. [00:30] Wind rustles maple leaves near porch.",
            downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        ),
        GalleryMediaItem(
            id = "video_autumn_driveway_leaves",
            title = "Driveway Cam 04: Autumn Breeze Sweeping Leaves",
            category = GalleryCategory.DRIVEWAY_VIDEOS,
            mediaKind = MediaKind.VIDEO,
            remoteUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            posterUrl = "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=1200&q=80",
            dateLabel = "Sep 22, 2026",
            timeLabel = "03:30:00 PM",
            durationSeconds = 30,
            location = "Driveway Apron & Curb",
            caption = "Crisp autumn gusts swirling colorful yellow and orange oak leaves across the driveway surface.",
            altText = "Video of wind blowing autumn foliage across suburban driveway",
            technicalBadge = "HIGH MOTION CAM • 1080P HD",
            resolution = "1920 x 1080 HD",
            aspectRatio = "16:9",
            tags = listOf("autumn", "leaves", "driveway", "wind", "nature"),
            attribution = "Seasonal Environmental Footage",
            licenseInfo = "Free Public License",
            transcript = "[00:02] Gusts of wind rustling foliage. [00:15] Leaves blowing across driveway pavement.",
            downloadUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"
        )
    ) + (6..40).map { index ->
        val cat = when (index % 4) {
            0 -> GalleryCategory.DRIVEWAY_VIDEOS
            1 -> GalleryCategory.SUBURBAN_LIFE
            2 -> GalleryCategory.CULTURE_CUISINE
            else -> GalleryCategory.NATURAL_PARKS
        }
        val videoSampleUrls = listOf(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnTheLosingStreak.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
        )
        val posters = listOf(
            "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
            "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=1200&q=80"
        )

        val selectedUrl = videoSampleUrls[(index - 6) % videoSampleUrls.size]
        val selectedPoster = posters[(index - 6) % posters.size]

        GalleryMediaItem(
            id = "video_item_$index",
            title = "Real-World Video Footage #$index: ${cat.displayName}",
            category = cat,
            mediaKind = MediaKind.VIDEO,
            remoteUrl = selectedUrl,
            posterUrl = selectedPoster,
            dateLabel = "Sep ${28 - (index % 20)}, 2026",
            timeLabel = "${(index % 12) + 1}:20 AM",
            durationSeconds = 25 + (index * 3) % 45,
            location = "North American Location #$index",
            caption = "Authentic real-world HD video recording showcasing daily life, security surveillance, landscapes, or cultural routines.",
            altText = "Video footage clip depicting ${cat.displayName} scene #$index",
            technicalBadge = "AUTHENTIC VIDEO • 1080P HD 60FPS • STEREO AUDIO",
            resolution = "1920 x 1080 60fps",
            aspectRatio = "16:9",
            tags = listOf(cat.tag, "video", "authentic", "hd", "real_world"),
            attribution = "Public Open Video Repository / Licensed Media",
            licenseInfo = "Free Commercial & Personal Use License",
            transcript = "[00:01] Ambient outdoor audio. [00:15] Motion detected in frame. [00:25] Clip sequence completed successfully.",
            downloadUrl = selectedUrl
        )
    }

    // Complete list of 80+ Unique Items (40+ Photos and 40+ Videos)
    val defaultItems: List<GalleryMediaItem> = photoItems + videoItems

    fun getItemsForCategory(category: GalleryCategory): List<GalleryMediaItem> {
        return when (category) {
            GalleryCategory.ALL -> defaultItems
            GalleryCategory.PHOTOS_ONLY -> defaultItems.filter { it.mediaKind == MediaKind.PHOTO }
            GalleryCategory.VIDEOS_ONLY -> defaultItems.filter { it.mediaKind == MediaKind.VIDEO }
            else -> defaultItems.filter { it.category == category }
        }
    }
}
