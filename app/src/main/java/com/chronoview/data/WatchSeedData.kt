package com.chronoview.data

object WatchSeedData {
    val watches = listOf(
        Watch(
            id = 1,
            name = "Aether Chronograph",
            brand = "Aurelius",
            category = WatchCategory.Luxury,
            price = 1849.0,
            rating = 4.9,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1523170335258-f5ed11844a49",
                "https://images.unsplash.com/photo-1546868871-7041f2a55e12",
                "https://images.unsplash.com/photo-1434056886845-dac89ffe9b56"
            ),
            baseDescription = "Swiss-crafted movement with a sapphire crystal and moonphase dial."
        ),
        Watch(
            id = 2,
            name = "Velocity X",
            brand = "Stratus",
            category = WatchCategory.Sport,
            price = 699.0,
            rating = 4.6,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1509048191080-d2e952122984",
                "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1",
                "https://images.unsplash.com/photo-1490367532201-b9bc1dc483f6"
            ),
            baseDescription = "High-resilience sport watch with 200m water resistance and GPS sync."
        ),
        Watch(
            id = 3,
            name = "Urban Minimal",
            brand = "NOVA",
            category = WatchCategory.Casual,
            price = 249.0,
            rating = 4.4,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1623998021450-85c8224a2550",
                "https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3",
                "https://images.unsplash.com/photo-1612817159949-195b6eb9e31a"
            ),
            baseDescription = "Slim silhouette with polished steel body for everyday elegance."
        ),
        Watch(
            id = 4,
            name = "Regal Tourbillon",
            brand = "Valmont",
            category = WatchCategory.Luxury,
            price = 3299.0,
            rating = 4.8,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1620625515032-6ed0c1790f84",
                "https://images.unsplash.com/photo-1511389026070-a14ae610a1be",
                "https://images.unsplash.com/photo-1547996160-81dfa63595aa"
            ),
            baseDescription = "Open-heart tourbillon complication with hand-stitched leather strap."
        ),
        Watch(
            id = 5,
            name = "Pulse Runner",
            brand = "Kinetic",
            category = WatchCategory.Sport,
            price = 399.0,
            rating = 4.5,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d",
                "https://images.unsplash.com/photo-1524805444758-089113d48a6d",
                "https://images.unsplash.com/photo-1511497584788-876760111969"
            ),
            baseDescription = "Performance-focused smartwatch hybrid with heart-rate analytics."
        ),
        Watch(
            id = 6,
            name = "Heritage Slate",
            brand = "Eon",
            category = WatchCategory.Casual,
            price = 319.0,
            rating = 4.3,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1524592094714-0f0654e20314",
                "https://images.unsplash.com/photo-1495857000853-fe46c8aefc30",
                "https://images.unsplash.com/photo-1609587312208-cea54be969e7"
            ),
            baseDescription = "Classic dial markers and a matte finish for modern professional style."
        )
    )
}
