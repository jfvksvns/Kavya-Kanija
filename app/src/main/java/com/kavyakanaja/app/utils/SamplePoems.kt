package com.kavyakanaja.app.utils

import com.kavyakanaja.app.model.Poem

/**
 * Local sample poems for development and offline use.
 * Replace or extend with API-fetched data in production.
 */
object SamplePoems {

    val poems = listOf(
        Poem(
            id = 1,
            titleEn = "Manku Thimmana Kagga",
            titleKn = "ಮಂಕುತಿಮ್ಮನ ಕಗ್ಗ",
            poet = "D.V. Gundappa",
            text = """ಕತ್ತಲೆಯ ನಡುವೆ ಒಂದು ಕಿರಣ ಮೂಡಿತು,
ಬಾಳಿನ ಬೆಳಕ ತೋರಿತು।
ಎದೆಯ ತುಂಬ ಭರವಸೆ ತುಂಬಿತು,
ಜೀವನ ಸಾರ್ಥಕ ಆಯಿತು॥""",
            category = "Philosophy",
            isFeatured = true
        ),
        Poem(
            id = 2,
            titleEn = "Nature's Beauty",
            titleKn = "ಪ್ರಕೃತಿಯ ಸೌಂದರ್ಯ",
            poet = "Kuvempu",
            text = """ಹಸಿರು ಕಾಡಿನ ನಡುವೆ ಹಳ್ಳ ಹರಿಯಿತು,
ಪಕ್ಷಿಗಳ ಹಾಡು ಕಿವಿಗೆ ತಾಗಿತು।
ಗಾಳಿ ತಂಗಾಳಿ ಮನಸು ತಣಿಸಿತು,
ಪ್ರಕೃತಿ ತಾಯಿ ಮಡಿಲಲ್ಲಿ ಮಲಗಿತು॥""",
            category = "Nature"
        ),
        Poem(
            id = 3,
            titleEn = "Mother's Love",
            titleKn = "ತಾಯಿಯ ಪ್ರೀತಿ",
            poet = "Belagere Krishnasharma",
            text = """ತಾಯಿ ಪ್ರೀತಿ ಅಗಾಧ ಸಾಗರ,
ಮಗುವ ನೋವು ಕಡಿಮೆ ಮಾಡುವ।
ಕಷ್ಟದಲ್ಲಿ ಕೈ ಹಿಡಿದು ನಡೆಸುವ,
ದೇವರ ರೂಪ ತಾಯಿ ಮಮತೆ॥""",
            category = "Family"
        ),
        Poem(
            id = 4,
            titleEn = "Motherland Karnataka",
            titleKn = "ಕನ್ನಡ ನಾಡು",
            poet = "Rashtrakavi Govind Pai",
            text = """ಕನ್ನಡ ನಾಡು ನಮ್ಮ ನಾಡು,
ಕನ್ನಡ ತಾಯಿ ನಮ್ಮ ತಾಯಿ।
ಇಲ್ಲಿನ ಮಣ್ಣು ಹೊನ್ನು ಮಣ್ಣು,
ಕನ್ನಡಿಗರಾಗಿ ಬಾಳೋಣ ತಾಣ॥""",
            category = "Patriotic",
            isFeatured = false
        )
    )
}