package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object GameQuestionGenerator {

    fun generateQuestions(gameId: String, level: Int, languageCode: String = "en"): List<GameQuestion> {
        val calendar = Calendar.getInstance()
        val lang = languageCode.lowercase()

        // Localized day and month helpers
        val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon...
        val monthIndex = calendar.get(Calendar.MONTH) // 0=Jan...
        val dayNumber = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val timePeriodCode = when (hour) {
            in 5..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..20 -> "evening"
            else -> "night"
        }

        return when (gameId) {
            "day_date" -> generateDayDateQuestions(level, lang, dayOfWeekIndex, monthIndex, dayNumber, timePeriodCode)
            "place_time" -> generatePlaceTimeQuestions(level, lang)
            "remember_objects" -> generateRememberObjectsQuestions(level, lang)
            "find_target" -> generateFindTargetQuestions(level, lang)
            "sequence_attention" -> generateSequenceAttentionQuestions(level, lang)
            "simple_pattern" -> generateSimplePatternQuestions(level, lang)
            "everyday_choice" -> generateEverydayChoiceQuestions(level, lang)
            else -> listOf(
                GameQuestion(
                    id = "generic_1",
                    promptKey = getLocalizedText(lang, "Which choice feels best?", "কোনটো বিকল্প সঠিক যেন লাগে?", "कौन सा विकल्प सबसे अच्छा लगता है?", "কোন বিকল্পটি সেরা মনে হয়?"),
                    promptTextFallback = getLocalizedText(lang, "Which choice feels best?", "কোনটো বিকল্প সঠিক যেন লাগে?", "कौन सा विकल्प सबसे अच्छा लगता है?", "কোন বিকল্পটি সেরা মনে হয়?"),
                    visualEmoji = "✨",
                    options = listOf("Option 1", "Option 2"),
                    correctIndex = 0
                )
            )
        }
    }

    private fun getLocalizedText(
        lang: String,
        en: String,
        asStr: String,
        hi: String,
        bn: String,
        mni: String? = null,
        kha: String? = null,
        lus: String? = null,
        nag: String? = null
    ): String {
        return when (lang) {
            "as" -> asStr
            "hi" -> hi
            "bn" -> bn
            "mni" -> mni ?: asStr
            "kha" -> kha ?: en
            "lus" -> lus ?: en
            "nag" -> nag ?: hi
            else -> en
        }
    }

    private fun getLocalizedDayName(dayIndex: Int, lang: String): String {
        // dayIndex: 1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat
        val enDays = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val asDays = listOf("দেওবাৰ", "সোমবাৰ", "মঙলবাৰ", "বুধবাৰ", "বৃহস্পতিবাৰ", "শুকুৰবাৰ", "শনিবাৰ")
        val hiDays = listOf("रविवार", "सोमवार", "मंगलवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार")
        val bnDays = listOf("রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার")
        val mniDays = listOf("নোংমাইজিং", "য়াইফবা নিংথৌকাবা", "লেইপাকপোকপা", "য়ুমশকৈশা", "শগোলসেন", "ঈরাই", "থাংজা")
        val khaDays = listOf("Sngi U Blei", "Sngi Nyngkong", "Sngi Ba-ar", "Sngi Ba-lai", "Sngi Saw", "Sngi Thohdieng", "Sngi Saitjain")
        val lusDays = listOf("Pathianni", "Thawhtanni", "Thawhlehni", "Nilaini", "Ningani", "Zirtawpni", "Inrinni")
        val nagDays = listOf("Deobar", "Sombar", "Mongolbar", "Budhbar", "Brihospotibar", "Shukrobar", "Shonibar")

        val idx = (dayIndex - 1).coerceIn(0, 6)
        return when (lang) {
            "as" -> asDays[idx]
            "hi" -> hiDays[idx]
            "bn" -> bnDays[idx]
            "mni" -> mniDays[idx]
            "kha" -> khaDays[idx]
            "lus" -> lusDays[idx]
            "nag" -> nagDays[idx]
            else -> enDays[idx]
        }
    }

    private fun getLocalizedMonthName(monthIndex: Int, lang: String): String {
        // monthIndex: 0=Jan .. 11=Dec
        val enMonths = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val asMonths = listOf("জানুৱাৰী", "ফেব্ৰুৱাৰী", "মাৰ্চ", "এপ্ৰিল", "মে'", "জুন", "জুলাই", "আগষ্ট", "ছেপ্টেম্বৰ", "অক্টোবৰ", "নৱেম্বৰ", "ডিচেম্বৰ")
        val hiMonths = listOf("जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितंबर", "अक्टूबर", "नवंबर", "दिसंबर")
        val bnMonths = listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
        val mniMonths = listOf("জানুৱারী", "ফেব্রুৱারী", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "ওগস্ত", "সেপ্তেম্বর", "ওক্টোবর", "নবেম্বর", "দিসেম্বর")
        val khaMonths = listOf("Kyllalyngkot", "Rymphang", "Lber", "Iaiong", "Jymmang", "Jylliew", "Naitung", "Nailar", "Nailur", "Risaw", "Naiwieng", "Nohprah")
        val lusMonths = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val nagMonths = enMonths

        val idx = monthIndex.coerceIn(0, 11)
        return when (lang) {
            "as" -> asMonths[idx]
            "hi" -> hiMonths[idx]
            "bn" -> bnMonths[idx]
            "mni" -> mniMonths[idx]
            "kha" -> khaMonths[idx]
            "lus" -> lusMonths[idx]
            "nag" -> nagMonths[idx]
            else -> enMonths[idx]
        }
    }

    private fun getTimePeriodName(code: String, lang: String): String {
        return when (code) {
            "morning" -> getLocalizedText(lang, "Morning", "ৰাতিপুৱা", "सुबह", "সকাল", "অয়ুক", "Mynstep", "Zing lam", "Phujor")
            "afternoon" -> getLocalizedText(lang, "Afternoon", "দুপৰীয়া", "दोपहर", "দুপুর", "নুমিদাং", "Mynsngi", "Chhun lam", "Dupor")
            "evening" -> getLocalizedText(lang, "Evening", "গধূলি", "शाम", "সন্ধ্যা", "নুমিদাংৱাই", "Janmiet", "Tlai lam", "Gothuli")
            else -> getLocalizedText(lang, "Night", "ৰাতি", "रात", "রাত", "অহিং", "Miet", "Zan lam", "Raat")
        }
    }

    private fun generateDayDateQuestions(
        level: Int,
        lang: String,
        dayOfWeekIndex: Int,
        monthIndex: Int,
        dayNumber: String,
        timePeriodCode: String
    ): List<GameQuestion> {
        val currentPeriod = getTimePeriodName(timePeriodCode, lang)
        val oppositePeriod = if (timePeriodCode == "morning") getTimePeriodName("night", lang) else getTimePeriodName("morning", lang)
        val todayName = getLocalizedDayName(dayOfWeekIndex, lang)
        val wrongDay1 = getLocalizedDayName(if (dayOfWeekIndex == 1) 4 else 1, lang)
        val wrongDay2 = getLocalizedDayName(if (dayOfWeekIndex == 6) 2 else 6, lang)
        val currentMonth = getLocalizedMonthName(monthIndex, lang)
        val wrongMonth1 = getLocalizedMonthName(if (monthIndex == 0) 6 else 0, lang)
        val wrongMonth2 = getLocalizedMonthName(if (monthIndex == 9) 3 else 9, lang)

        val nextDayIndex = if (dayOfWeekIndex == 7) 1 else dayOfWeekIndex + 1
        val prevDayIndex = if (dayOfWeekIndex == 1) 7 else dayOfWeekIndex - 1
        val nextDayName = getLocalizedDayName(nextDayIndex, lang)
        val prevDayName = getLocalizedDayName(prevDayIndex, lang)

        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "dd_1_1",
                    promptKey = getLocalizedText(lang, "What period of the day is it right now?", "বৰ্তমান দিনটোৰ কোনটো সময়?", "अभी दिन का कौन सा समय है?", "এখন দিনের কোন সময়?", "হৌজিক নোংমগী করম্বা মতমনি?", "Mynta dei ka por aiu ha ka sngi?", "Tunah hian eng hun nge ni?", "Etu time te din laga kun time asey?"),
                    promptTextFallback = getLocalizedText(lang, "What period of the day is it right now?", "বৰ্তমান দিনটোৰ কোনটো সময়?", "अभी दिन का कौन सा समय है?", "এখন দিনের কোন সময়?", "হৌজিক নোংমগী করম্বা মতমনি?", "Mynta dei ka por aiu ha ka sngi?", "Tunah hian eng hun nge ni?", "Etu time te din laga kun time asey?"),
                    visualEmoji = if (timePeriodCode == "night" || timePeriodCode == "evening") "🌙" else "☀️",
                    options = listOf(currentPeriod, oppositePeriod),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "dd_1_2",
                    promptKey = getLocalizedText(lang, "Which day is it today?", "আজি কোনটো বাৰ?", "आज कौन सा दिन है?", "আজ কী বার?", "ঙসি করম্বা নুমিৎনো?", "Mynta dei kano ka sngi?", "Vawiin eng ni nge?", "Aji kun din asey?"),
                    promptTextFallback = getLocalizedText(lang, "Which day is it today?", "আজি কোনটো বাৰ?", "आज कौन सा दिन है?", "আজ কী বার?", "ঙসি করম্বা নুমিৎনো?", "Mynta dei kano ka sngi?", "Vawiin eng ni nge?", "Aji kun din asey?"),
                    visualEmoji = "📅",
                    options = listOf(todayName, wrongDay1),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "dd_2_1",
                    promptKey = getLocalizedText(lang, "Which month are we in currently?", "বৰ্তমান কোনটো মাহ চলি আছে?", "वर्तमान में कौन सा महीना चल रहा है?", "বর্তমানে কোন মাস চলছে?", "হৌজিক করম্বা থানি?", "Mynta dei uba kumno u bnai?", "Tunah hian eng thla nge kan hman?", "Etu kun mahina choli asey?"),
                    promptTextFallback = getLocalizedText(lang, "Which month are we in currently?", "বৰ্তমান কোনটো মাহ চলি আছে?", "वर्तमान में कौन सा महीना चल रहा है?", "বর্তমানে কোন মাস চলছে?", "হৌজিক করম্বা থানি?", "Mynta dei uba kumno u bnai?", "Tunah hian eng thla nge kan hman?", "Etu kun mahina choli asey?"),
                    visualEmoji = "🗓️",
                    options = listOf(currentMonth, wrongMonth1, wrongMonth2),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "dd_2_2",
                    promptKey = getLocalizedText(lang, "What is today's day of the week?", "আজি সপ্তাহৰ কোনটো দিন?", "आज सप्ताह का कौन सा दिन है?", "আজ সপ্তাহের কোন দিন?", "ঙসি চয়োলগী করম্বা নুমিৎনো?", "Mynta ka taiew dei kano ka sngi?", "Vawiin hi kar chhung ni eng nge?", "Aji hapta laga kun din asey?"),
                    promptTextFallback = getLocalizedText(lang, "What is today's day of the week?", "আজি সপ্তাহৰ কোনটো দিন?", "आज सप्ताह का कौन सा दिन है?", "আজ সপ্তাহের কোন দিন?", "ঙসি চয়োলগী করম্বা নুমিৎনো?", "Mynta ka taiew dei kano ka sngi?", "Vawiin hi kar chhung ni eng nge?", "Aji hapta laga kun din asey?"),
                    visualEmoji = "☀️",
                    options = listOf(todayName, wrongDay1, wrongDay2),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "dd_2_3",
                    promptKey = getLocalizedText(lang, "What day comes after today ($todayName)?", "আজিৰ ($todayName) পিছৰ দিনটো কি?", "आज ($todayName) के बाद कौन सा दिन आता है?", "আজকের ($todayName) পরের দিনটি কী?", "ঙসিগী ($todayName) মতুংগী নুমিৎ অদু করিনো?", "Kano ka sngi ban wan hadien mynta ($todayName)?", "Vawiin ($todayName) hnuah eng ni nge lo thleng ang?", "Aji ($todayName) pichete kun din ahibo?"),
                    promptTextFallback = getLocalizedText(lang, "What day comes after today ($todayName)?", "আজিৰ ($todayName) পিছৰ দিনটো কি?", "आज ($todayName) के बाद कौन सा दिन आता है?", "আজকের ($todayName) পরের দিনটি কী?", "ঙসিগী ($todayName) মতুংগী নুমিৎ অদু করিনো?", "Kano ka sngi ban wan hadien mynta ($todayName)?", "Vawiin ($todayName) hnuah eng ni nge lo thleng ang?", "Aji ($todayName) pichete kun din ahibo?"),
                    visualEmoji = "⏩",
                    options = listOf(nextDayName, wrongDay1, wrongDay2),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "dd_3_1",
                    promptKey = getLocalizedText(lang, "Identify today's date and month:", "আজিৰ তাৰিখ আৰু মাহ বাচক:", "आज की तारीख और महीना पहचानें:", "আজকের তারিখ ও মাস সনাক্ত করুন:", "ঙসিগী তারিখ অমসুং থা খল্লু:", "Jied ia ka tarik bad u bnai jong mynta:", "Vawiin ni thla leh ni zawng chhuak rawh:", "Aji laga tarikh aru mahina chunibo:"),
                    promptTextFallback = getLocalizedText(lang, "Identify today's date and month:", "আজিৰ তাৰিখ আৰু মাহ বাচক:", "आज की तारीख और महीना पहचानें:", "আজকের তারিখ ও মাস সনাক্ত করুন:", "ঙসিগী তারিখ অমসুং থা খল্লু:", "Jied ia ka tarik bad u bnai jong mynta:", "Vawiin ni thla leh ni zawng chhuak rawh:", "Aji laga tarikh aru mahina chunibo:"),
                    visualEmoji = "📆",
                    options = listOf("$dayNumber $currentMonth", "15 January", "22 October", "1 May"),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "dd_3_2",
                    promptKey = getLocalizedText(lang, "If today is $todayName, which day was yesterday?", "যদি আজি $todayName হয়, কালি কোন দিন আছিল?", "यदि आज $todayName है, तो कल कौन सा दिन था?", "আজ যদি $todayName হয়, তবে গতকাল কী বার ছিল?", "ঙসি $todayName ওইরবদি ঙরাং করম্বা নুমিৎ ওইখিগে?", "Lada mynta dei $todayName, hynnin dei kano ka sngi?", "Vawiin $todayName a nih chuan nimin kha eng ni nge?", "Aji $todayName asey koile, kali kun din thakishey?"),
                    promptTextFallback = getLocalizedText(lang, "If today is $todayName, which day was yesterday?", "যদি আজি $todayName হয়, কালি কোন দিন আছিল?", "यदि आज $todayName है, तो कल कौन सा दिन था?", "আজ যদি $todayName হয়, তবে গতকাল কী বার ছিল?", "ঙসি $todayName ওইরবদি ঙরাং করম্বা নুমিৎ ওইখিগে?", "Lada mynta dei $todayName, hynnin dei kano ka sngi?", "Vawiin $todayName a nih chuan nimin kha eng ni nge?", "Aji $todayName asey koile, kali kun din thakishey?"),
                    visualEmoji = "⏪",
                    options = listOf(prevDayName, wrongDay1, wrongDay2, nextDayName),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "dd_3_3",
                    promptKey = getLocalizedText(lang, "Which season brings warm monsoon rains to North East India?", "উত্তৰ-পূব ভাৰতলৈ কোন ঋতুৱে বাৰিষাৰ বৰষুণ আনে?", "पूर्वोत्तर भारत में कौन सा मौसम मानसूनी बारिश लाता है?", "উত্তর-পূর্ব ভারতে কোন ঋতুতে বর্ষা নামে?", "অৱাং-নোংপোক ভারত্তা করম্বা ঋতুনা নোংগী মতম পুরকই?", "Kano ka samoi kaba wanrah ia u slap monsoon ha North East?", "Eng season hian nge North East-ah ruah tam tak rawn thlen?", "North East te kun mausam te bhal boroxun ahey?"),
                    promptTextFallback = getLocalizedText(lang, "Which season brings warm monsoon rains to North East India?", "উত্তৰ-পূব ভাৰতলৈ কোন ঋতুৱে বাৰিষাৰ বৰষুণ আনে?", "पूर्वोत्तर भारत में कौन सा मौसम मानसूनी बारिश लाता है?", "উত্তর-পূর্ব ভারতে কোন ঋতুতে বর্ষা নামে?", "অৱাং-নোংপোক ভারত্তা করম্বা ঋতুনা নোংগী মতম পুরকই?", "Kano ka samoi kaba wanrah ia u slap monsoon ha North East?", "Eng season hian nge North East-ah ruah tam tak rawn thlen?", "North East te kun mausam te bhal boroxun ahey?"),
                    visualEmoji = "🌧️",
                    options = listOf(
                        getLocalizedText(lang, "Monsoon / Rainy", "বাৰিষা / বাৰিষাকাল", "मानसून / वर्षा ऋतु", "বর্ষাকাল", "নোংজু থা", "Samoi slap", "Ruahtui tlak lai", "Boroxun Mausam"),
                        getLocalizedText(lang, "Mid-Winter Snow", "শীতকালৰ বৰফ", "शीतकाल", "শীতকাল", "নিংথৌ থা", "Tlang thiah", "Thlasik", "Thanda Din"),
                        getLocalizedText(lang, "Dry Desert Heat", "মৰুভূমিৰ গৰম", "मरुस्थलीय गर्मी", "মরুভূমির গরম", "মরুময় মতম", "Por shit", "Lal lai", "Garmi"),
                        getLocalizedText(lang, "Spring Frost", "বসন্তৰ নিয়ৰ", "बसंत", "বসন্তকাল", "বসন্ত", "Por pyrem", "Favang", "Bohag")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generatePlaceTimeQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "pt_1_1",
                    promptKey = getLocalizedText(lang, "Where in the house do we sleep and rest peacefully?", "ঘৰৰ ক'ত আমি শান্তিত শোওঁ আৰু বিশ্ৰাম লওঁ?", "घर में हम शांति से कहाँ सोते और आराम करते हैं?", "ঘরের কোথায় আমরা শান্তিতে ঘুমাই ও বিশ্রাম নিই?", "য়ুমগী করম্বা মফমদা ঐখোয় শান্তিনা তুম্বা অমসুং পোথারবা য়ারিবগে?", "Ha kano ka kamra ha iing ngi thiah bad shongthait suk?", "In chhung khawi laiah nge hahdam taka kan mut thin?", "Ghor te kun jaga te aram se hui kene shanti paye?"),
                    promptTextFallback = getLocalizedText(lang, "Where in the house do we sleep and rest peacefully?", "ঘৰৰ ক'ত আমি শান্তিত শোওঁ আৰু বিশ্ৰাম লওঁ?", "घर में हम शांति से कहाँ सोते और आराम करते हैं?", "ঘরের কোথায় আমরা শান্তিতে ঘুমাই ও বিশ্রাম নিই?", "য়ুমগী করম্বা মফমদা ঐখোয় শান্তিনা তুম্বা অমসুং পোথারবা য়ারিবগে?", "Ha kano ka kamra ha iing ngi thiah bad shongthait suk?", "In chhung khawi laiah nge hahdam taka kan mut thin?", "Ghor te kun jaga te aram se hui kene shanti paye?"),
                    visualEmoji = "🛏️",
                    options = listOf(
                        getLocalizedText(lang, "Cozy Bedroom 🛏️", "শোৱা কোঠা 🛏️", "शयनकक्ष (बेडरूम) 🛏️", "শোবার ঘর 🛏️", "কা নকশিনবা তুম্ফম 🛏️", "Kamra thiah 🛏️", "Mutna pindan 🛏️", "Hui laga room 🛏️"),
                        getLocalizedText(lang, "Kitchen Stove 🔥", "ৰান্ধনশালৰ চুলা 🔥", "रसोई का चूल्हा 🔥", "রান্নাঘর 🔥", "চাকখুম 🔥", "Rynsan shetja 🔥", "Chhuanfung 🔥", "Chula 🔥")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "pt_1_2",
                    promptKey = getLocalizedText(lang, "Where do we prepare warm tea and food?", "আমি ক'ত গৰম চাহ আৰু খাদ্য প্ৰস্তুত কৰোঁ?", "हम गर्म चाय और खाना कहाँ बनाते हैं?", "আমরা কোথায় গরম চা এবং খাবার প্রস্তুত করি?", "ঐখোয়না শাফবা চা অমসুং চিঞ্জাক কদাইদা শেম্বগে?", "Hangno ngi shet sha bad jingbam?", "Khawi laiah nge thingpui leh chaw kan chhum thin?", "Ghor te cha aru bhat kuntu jaga te bonai?"),
                    promptTextFallback = getLocalizedText(lang, "Where do we prepare warm tea and food?", "আমি ক'ত গৰম চাহ আৰু খাদ্য প্ৰস্তুত কৰোঁ?", "हम गर्म चाय और खाना कहाँ बनाते हैं?", "আমরা কোথায় গরম চা এবং খাবার প্রস্তুত করি?", "ঐখোয়না শাফবা চা অমসুং চিঞ্জাক কদাইদা শেম্বগে?", "Hangno ngi shet sha bad jingbam?", "Khawi laiah nge thingpui leh chaw kan chhum thin?", "Ghor te cha aru bhat kuntu jaga te bonai?"),
                    visualEmoji = "🫖",
                    options = listOf(
                        getLocalizedText(lang, "Kitchen 🫖", "ৰান্ধনিঘৰ 🫖", "रसोईघर 🫖", "রান্নাঘর 🫖", "চাকখুম 🫖", "Rynsan shetja 🫖", "Chhanchhung 🫖", "Kitchen 🫖"),
                        getLocalizedText(lang, "Bathroom 🚿", "গা-ধোৱা ঘৰ 🚿", "स्नानघर 🚿", "গোসলখানা 🚿", "ইরুজবা মফম 🚿", "Kamra sumbad 🚿", "Inthiarna 🚿", "Bathroom 🚿")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "pt_2_1",
                    promptKey = getLocalizedText(lang, "In which North Eastern state is the Brahmaputra River and Kaziranga located?", "ব্ৰহ্মপুত্ৰ নদী আৰু কাজিৰঙা উত্তৰ-পূবৰ কোনখন ৰাজ্যত অৱস্থিত?", "ब्रह्मपुत्र नदी और काजीरंगा किस पूर्वोत्तर राज्य में स्थित हैं?", "ব্রহ্মপুত্র নদ ও কাজিরাঙা উত্তর-পূর্বের কোন রাজ্যে অবস্থিত?", "ব্রহ্মপুত্র তুরেল অমসুং কাজিরঙ্গা অৱাং-নোংপোক্কী করম্বা রাজ্যদা লৈবগে?", "Ha kano ka jylla jong ka North East don ka Wah Brahmaputra bad Kaziranga?", "Brahmaputra Luipui leh Kaziranga hi North East state khawi laiah nge a awm?", "Brahmaputra Nodi aru Kaziranga kun state te asey?"),
                    promptTextFallback = getLocalizedText(lang, "In which North Eastern state is the Brahmaputra River and Kaziranga located?", "ব্ৰহ্মপুত্ৰ নদী আৰু কাজিৰঙা উত্তৰ-পূবৰ কোনখন ৰাজ্যত অৱস্থিত?", "ब्रह्मपुत्र नदी और काजीरंगा किस पूर्वोत्तर राज्य में स्थित हैं?", "ব্রহ্মপুত্র নদ ও কাজিরাঙা উত্তর-পূর্বের কোন রাজ্যে অবস্থিত?", "ব্রহ্মপুত্র তুরেল অমসুং কাজিরঙ্গা অৱাং-নোংপোক্কী করম্বা রাজ্যদা লৈবগে?", "Ha kano ka jylla jong ka North East don ka Wah Brahmaputra bad Kaziranga?", "Brahmaputra Luipui leh Kaziranga hi North East state khawi laiah nge a awm?", "Brahmaputra Nodi aru Kaziranga kun state te asey?"),
                    visualEmoji = "🦏",
                    options = listOf(
                        getLocalizedText(lang, "Assam", "অসম", "असम", "আসাম", "অসাম", "Assam", "Assam", "Assam"),
                        getLocalizedText(lang, "Goa", "গোৱা", "गोवा", "গোয়া", "গোৱা", "Goa", "Goa", "Goa"),
                        getLocalizedText(lang, "Rajasthan", "ৰাজস্থান", "राजस्थान", "রাজস্থান", "রাজস্থান", "Rajasthan", "Rajasthan", "Rajasthan")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "pt_2_2",
                    promptKey = getLocalizedText(lang, "Where are the famous Living Root Bridges and misty hills found?", "বিখ্যাত জীৱন্ত শিপাৰ সাঁকো (Living Root Bridges) আৰু মেঘাচ্ছন্ন পাহাৰ ক'ত পোৱা যায়?", "प्रसिद्ध जीवित जड़ पुल (Living Root Bridges) कहाँ पाए जाते हैं?", "বিখ্যাত জীবন্ত মূলের সেতু (Living Root Bridges) কোথায় পাওয়া যায়?", "মিংচৎ লৈবা উচেক থৌরিগী থোং অমসুং নোংচুংবা চীং কদাইদা উবগে?", "Hangno ngi lap ia ki jingkieng tynrai (Living Root Bridges) bad ki lum slap?", "Living Root Bridges hmingthang tak hi khawi state-ah nge a awm?", "Living Root Bridge aru pahaad khan kun jaga te asey?"),
                    promptTextFallback = getLocalizedText(lang, "Where are the famous Living Root Bridges and misty hills found?", "বিখ্যাত জীৱন্ত শিপাৰ সাঁকো (Living Root Bridges) আৰু মেঘাচ্ছন্ন পাহাৰ ক'ত পোৱা যায়?", "प्रसिद्ध जीवित जड़ पुल (Living Root Bridges) कहाँ पाए जाते हैं?", "বিখ্যাত জীবন্ত মূলের সেতু (Living Root Bridges) কোথায় পাওয়া যায়?", "মিংচৎ লৈবা উচেক থৌরিগী থোং অমসুং নোংচুংবা চীং কদাইদা উবগে?", "Hangno ngi lap ia ki jingkieng tynrai (Living Root Bridges) bad ki lum slap?", "Living Root Bridges hmingthang tak hi khawi state-ah nge a awm?", "Living Root Bridge aru pahaad khan kun jaga te asey?"),
                    visualEmoji = "🌿",
                    options = listOf(
                        getLocalizedText(lang, "Meghalaya", "মেঘালয়", "मेघालय", "মেঘালয়", "মেঘালয়", "Meghalaya", "Meghalaya", "Meghalaya"),
                        getLocalizedText(lang, "Gujarat", "গুজৰাট", "गुजरात", "গুজরাট", "গুজরাত", "Gujarat", "Gujarat", "Gujarat"),
                        getLocalizedText(lang, "Haryana", "হাৰিয়ানা", "हरियाणा", "হরিয়ানা", "হরিয়ানা", "Haryana", "Haryana", "Haryana")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "pt_2_3",
                    promptKey = getLocalizedText(lang, "Where do we keep clean drinking water at home?", "ঘৰত পৰিষ্কাৰ খোৱাপানী ক'ত ৰখা হয়?", "घर में साफ पीने का पानी कहाँ रखा जाता है?", "বাড়িতে বিশুদ্ধ খাবার জল কোথায় রাখা হয়?", "য়ুমদা পরিষ্কাৰ ওইবা থক্নবা ঈশিং কদাইদা থম্বগে?", "Hangno ngi buh ia ka umbam ba khuid ha iing?", "In chhungah tui thianghlim khawi laiah nge kan dah thin?", "Ghor te saaf paani kuntu jaga te rakhe?"),
                    promptTextFallback = getLocalizedText(lang, "Where do we keep clean drinking water at home?", "ঘৰত পৰিষ্কাৰ খোৱাপানী ক'ত ৰখা হয়?", "घर में साफ पीने का पानी कहाँ रखा जाता है?", "বাড়িতে বিশুদ্ধ খাবার জল কোথায় রাখা হয়?", "য়ুমদা পরিষ্কাৰ ওইবা থক্নবা ঈশিং কদাইদা থম্বগে?", "Hangno ngi buh ia ka umbam ba khuid ha iing?", "In chhungah tui thianghlim khawi laiah nge kan dah thin?", "Ghor te saaf paani kuntu jaga te rakhe?"),
                    visualEmoji = "💧",
                    options = listOf(
                        getLocalizedText(lang, "Clean Water Filter / Jug 💧", "পৰিষ্কাৰ ফিল্টাৰ বা জগ 💧", "साफ वाटर फिल्टर या जग 💧", "পরিষ্কার জলের ফিল্টার 💧", "শেংলবা ঈশিং ফিল্তর 💧", "Filter um / Khiew um 💧", "Tui dahna thianghlim 💧", "Saaf Filter Jug 💧"),
                        getLocalizedText(lang, "Shoe Rack 👟", "জোতাৰ আলমাৰী 👟", "जूते रखने का रैक 👟", "জুতো রাখার তাক 👟", "খুৎশম থম্ফম 👟", "Rynsan juti 👟", "Pheikhawk dahna 👟", "Juta Rack 👟"),
                        getLocalizedText(lang, "Laundry Basket 🧺", "কাপোৰ ধোৱা বাস্কেট 🧺", "कपड़े की टोकरी 🧺", "কাপড়ের ঝুড়ি 🧺", "পোৎলম বোক্স 🧺", "Kriah jain 🧺", "Hmunphiah dahna 🧺", "Dhula Kapor 🧺")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "pt_3_1",
                    promptKey = getLocalizedText(lang, "Loktak Lake, famous for floating Phumdis, is in which state?", "ভাসমান ফুমদীৰ বাবে বিখ্যাত লোকটক হ্ৰদ কোনখন ৰাজ্যত অৱস্থিত?", "तैरते हुए फुमदी के लिए प्रसिद्ध लोकतक झील किस राज्य में है?", "ভাসমান ফুমদির জন্য বিখ্যাত লোকটক হ্রদ কোন রাজ্যে অবস্থিত?", "ফুমদি তুম্লগা য়াওবা লোকতাক পাত করম্বা রাজ্যদা লৈবগে?", "Ka Wah Loktak kaba don ki Phumdi kiba per ka don ha kano ka jylla?", "Loktak Dil, Phumdi lanna awm hi khawi state-ah nge a awm?", "Loktak Lake kun state te asey?"),
                    promptTextFallback = getLocalizedText(lang, "Loktak Lake, famous for floating Phumdis, is in which state?", "ভাসমান ফুমদীৰ বাবে বিখ্যাত লোকটক হ্ৰদ কোনখন ৰাজ্যত অৱস্থিত?", "तैरते हुए फुमदी के लिए प्रसिद्ध लोकतक झील किस राज्य में है?", "ভাসমান ফুমদির জন্য বিখ্যাত লোকটক হ্রদ কোন রাজ্যে অবস্থিত?", "ফুমদি তুম্লগা য়াওবা লোকতাক পাত করম্বা রাজ্যদা লৈবগে?", "Ka Wah Loktak kaba don ki Phumdi kiba per ka don ha kano ka jylla?", "Loktak Dil, Phumdi lanna awm hi khawi state-ah nge a awm?", "Loktak Lake kun state te asey?"),
                    visualEmoji = "🏞️",
                    options = listOf(
                        getLocalizedText(lang, "Manipur", "মণিপুৰ", "मणिपुर", "মণিপুর", "মণিপুর", "Manipur", "Manipur", "Manipur"),
                        getLocalizedText(lang, "Punjab", "পাঞ্জাব", "पंजाब", "পাঞ্জাব", "পঞ্জাব", "Punjab", "Punjab", "Punjab"),
                        getLocalizedText(lang, "Kerala", "কেৰালা", "केरल", "কেরালা", "কেরলা", "Kerala", "Kerala", "Kerala"),
                        getLocalizedText(lang, "Bihar", "বিহাৰ", "बिहार", "বিহার", "বিহার", "Bihar", "Bihar", "Bihar")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "pt_3_2",
                    promptKey = getLocalizedText(lang, "The capital of Nagaland known for the Hornbill Festival is:", "হৰ্ণবিল উৎসৱৰ বাবে পৰিচিত নাগালেণ্ডৰ ৰাজধানী কি?", "हॉर्नबिल महोत्सव के लिए प्रसिद्ध नागालैंड की राजधानी है:", "হর্নবিল উৎসবের জন্য পরিচিত নাগাল্যান্ডের রাজধানী কোনটি?", "হর্নবিল কুহ্মৈগীদমক খংনবা নাগালেন্দগী কোনুং অদু করিনো?", "Ka nongbah jong ka Nagaland ba paw ha ka Hornbill Festival dei ka:", "Hornbill Festival avanga hmingthang Nagaland khawpui chu:", "Nagaland laga Hornbill Festival capital kun asey?"),
                    promptTextFallback = getLocalizedText(lang, "The capital of Nagaland known for the Hornbill Festival is:", "হৰ্ণবিল উৎসৱৰ বাবে পৰিচিত নাগালেণ্ডৰ ৰাজধানী কি?", "हॉर्नबिल महोत्सव के लिए प्रसिद्ध नागालैंड की राजधानी है:", "হর্নবিল উৎসবের জন্য পরিচিত নাগাল্যান্ডের রাজধানী কোনটি?", "হর্নবিল কুহ্মৈগীদমক খংনবা নাগালেন্দগী কোনুং অদু করিনো?", "Ka nongbah jong ka Nagaland ba paw ha ka Hornbill Festival dei ka:", "Hornbill Festival avanga hmingthang Nagaland khawpui chu:", "Nagaland laga Hornbill Festival capital kun asey?"),
                    visualEmoji = "🦜",
                    options = listOf(
                        getLocalizedText(lang, "Kohima", "কোহিমা", "कोहिमा", "কোহিমা", "কোহিমা", "Kohima", "Kohima", "Kohima"),
                        getLocalizedText(lang, "Agartala", "আগৰতলা", "अगरतला", "আগরতলা", "আগরতলা", "Agartala", "Agartala", "Agartala"),
                        getLocalizedText(lang, "Shillong", "শ্বিলং", "शिलांग", "শিলং", "শিলং", "Shillong", "Shillong", "Shillong"),
                        getLocalizedText(lang, "Aizawl", "আইজল", "आइजोल", "আইজল", "আইজল", "Aizawl", "Aizawl", "Aizawl")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "pt_3_3",
                    promptKey = getLocalizedText(lang, "At 8:00 in the morning, which activity fits best?", "ৰাতিপুৱা ৮:০০ বজাত কোনটো কাৰ্য্য আটাইতকৈ উপযুক্ত?", "सुबह के 8:00 बजे कौन सी गतिविधि सबसे उपयुक्त है?", "সকাল ৮:০০ টায় কোন কাজটি সবচেয়ে মানানসই?", "অয়ুক্কী পুং ৮ তাবদা করম্বা থবকনা খ্বাইদগী চুনবগে?", "Ha ka por 8:00 mynstep, kano ka kam kaba iahap tam?", "Zing dar 8:00-ah eng tih nge inhmeh ber?", "Phujor 8:00 te kun kaam sobse bhal lage?"),
                    promptTextFallback = getLocalizedText(lang, "At 8:00 in the morning, which activity fits best?", "ৰাতিপুৱা ৮:০০ বজাত কোনটো কাৰ্য্য আটাইতকৈ উপযুক্ত?", "सुबह के 8:00 बजे कौन सी गतिविधि सबसे उपयुक्त है?", "সকাল ৮:০০ টায় কোন কাজটি সবচেয়ে মানানসই?", "অয়ুক্কী পুং ৮ তাবদা করম্বা থবকনা খ্বাইদগী চুনবগে?", "Ha ka por 8:00 mynstep, kano ka kam kaba iahap tam?", "Zing dar 8:00-ah eng tih nge inhmeh ber?", "Phujor 8:00 te kun kaam sobse bhal lage?"),
                    visualEmoji = "🥣",
                    options = listOf(
                        getLocalizedText(lang, "Morning tea & healthy breakfast 🥣", "ৰাতিপুৱাৰ চাহ আৰু পুষ্টিকৰ জলপান 🥣", "सुबह की चाय और नाश्ता 🥣", "সকালের চা ও জলখাবার 🥣", "অয়ুক্কী চা অমসুং চানবা 🥣", "Dih sha bad bam ja step 🥣", "Thingpui leh tukthuan ei 🥣", "Phujor cha aru nasta 🥣"),
                        getLocalizedText(lang, "Midnight deep sleep 🛌", "মাজনিশাৰ গভীৰ টোপনি 🛌", "आधी रात की नींद 🛌", "গভীর ঘুম 🛌", "অহিংগী অহিং তুম্বা 🛌", "Thiah miet 🛌", "Zan mut hilh 🛌", "Rati laga ghum 🛌"),
                        getLocalizedText(lang, "Turning off all house lights 💡", "ঘৰৰ সকলো লাইট নুমুৱাই দিয়া 💡", "घर की सभी बत्तियाँ बुझाना 💡", "সব আলো নিভিয়ে দেওয়া 💡", "মৈ পুম্নমক মুথৎপা 💡", "Pynlip ding 💡", "Khawvel ti meng lo 💡", "Batti bondha kora 💡"),
                        getLocalizedText(lang, "Watching night stars 🌌", "নিশাৰ আকাশৰ তৰা চোৱা 🌌", "रात के तारे देखना 🌌", "রাতের তারা দেখা 🌌", "অহিংগী থৱানমিচাক য়েংবা 🌌", "Peit khlur miet 🌌", "Arsi thlir 🌌", "Tara sabo 🌌")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generateRememberObjectsQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "ro_1_1",
                    promptKey = getLocalizedText(lang, "Which object did you just see?", "আপুনি এতিয়াই কোনটো বস্তু দেখিলে?", "आपने अभी कौन सी वस्तु देखी?", "আপনি এইমাত্র কোন বস্তুটি দেখলেন?", "হৌজিকতমক নহাক্না করম্বা পোৎলম উখিবগে?", "Kano ka tiar kaba phi dang shu iohi?", "Eng thil nge i hmuh zawh chiah kha?", "Apuni etiya kun saman dekhishey?"),
                    promptTextFallback = getLocalizedText(lang, "Which object did you just see?", "আপুনি এতিয়াই কোনটো বস্তু দেখিলে?", "आपने अभी कौन सी वस्तु देखी?", "আপনি এইমাত্র কোন বস্তুটি দেখলেন?", "হৌজিকতমক নহাক্না করম্বা পোৎলম উখিবগে?", "Kano ka tiar kaba phi dang shu iohi?", "Eng thil nge i hmuh zawh chiah kha?", "Apuni etiya kun saman dekhishey?"),
                    visualEmoji = "🍵",
                    options = listOf(
                        getLocalizedText(lang, "Tea Cup 🍵", "চাহৰ কাপ 🍵", "चाय का कप 🍵", "চায়ের কাপ 🍵", "চা কপ 🍵", "Khiew sha 🍵", "Thingpui no 🍵", "Cha Cup 🍵"),
                        getLocalizedText(lang, "Umbrella ☂️", "ছাতি ☂️", "छाता ☂️", "ছাতা ☂️", "শমব্রাক ☂️", "Chatri ☂️", "Nihliap ☂️", "Chati ☂️")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ro_1_2",
                    promptKey = getLocalizedText(lang, "Which musical instrument was displayed?", "কোনটো বাদ্যযন্ত্ৰ প্ৰদৰ্শন কৰা হৈছিল?", "कौन सा वाद्ययंत्र दिखाया गया था?", "কোন বাদ্যযন্ত্রটি প্রদর্শিত হয়েছিল?", "করম্বা খোংজেল পোকহনবা পোৎলম উৎখিবগে?", "Kano ka jingtem ba la pyni?", "Eng rimawi hmanraw nge an tarlan kha?", "Kun bajana saman dekhishey?"),
                    promptTextFallback = getLocalizedText(lang, "Which musical instrument was displayed?", "কোনটো বাদ্যযন্ত্ৰ প্ৰদৰ্শন কৰা হৈছিল?", "कौन सा वाद्ययंत्र दिखाया गया था?", "কোন বাদ্যযন্ত্রটি প্রদর্শিত হয়েছিল?", "করম্বা খোংজেল পোকহনবা পোৎলম উৎখিবগে?", "Kano ka jingtem ba la pyni?", "Eng rimawi hmanraw nge an tarlan kha?", "Kun bajana saman dekhishey?"),
                    visualEmoji = "🔔",
                    options = listOf(
                        getLocalizedText(lang, "Golden Bell 🔔", "সোণালী ঘণ্টা 🔔", "सुनहरी घंटी 🔔", "সোনার ঘণ্টা 🔔", "সনাগী ঘন্তা 🔔", "Shakuria ksiar 🔔", "Dar 🔔", "Ghanti 🔔"),
                        getLocalizedText(lang, "Car Key 🔑", "গাড়ীৰ চাবি 🔑", "कार की चाबी 🔑", "গাড়ির চাবি 🔑", "গাড়ীগী চাবী 🔑", "Chabi kali 🔑", "Chabi 🔑", "Gari Chabi 🔑")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "ro_2_1",
                    promptKey = getLocalizedText(lang, "Which fruit was shown in the memory set?", "স্মৃতিৰ তালিকাত কোনটো ফল দেখুওৱা হৈছিল?", "स्मृति सेट में कौन सा फल दिखाया गया था?", "মেমোরি সেটে কোন ফলটি দেখানো হয়েছিল?", "নীংশিংবা সেত্তা করম্বা হৈ দেখহন্বগে?", "Uba kumno u soh ba la pyni?", "Eng theihai nge an tihlan kha?", "Kun phal dekhishey?"),
                    promptTextFallback = getLocalizedText(lang, "Which fruit was shown in the memory set?", "স্মৃতিৰ তালিকাত কোনটো ফল দেখুওৱা হৈছিল?", "स्मृति सेट में कौन सा फल दिखाया गया था?", "মেমোরি সেটে কোন ফলটি দেখানো হয়েছিল?", "নীংশিংবা সেত্তা করম্বা হৈ দেখহন্বগে?", "Uba kumno u soh ba la pyni?", "Eng theihai nge an tihlan kha?", "Kun phal dekhishey?"),
                    visualEmoji = "🍎",
                    options = listOf(
                        getLocalizedText(lang, "Red Apple 🍎", "ৰঙা আপেল 🍎", "लाल सेब 🍎", "লাল আপেল 🍎", "অঙাংবা সেও 🍎", "Soh saw 🍎", "Apple sen 🍎", "Lal Apple 🍎"),
                        getLocalizedText(lang, "Chili Pepper 🌶️", "জ্বলা জলকীয়া 🌶️", "हरी मिर्च 🌶️", "লঙ্কা 🌶️", "মোরাম 🌶️", "Sohmynken 🌶️", "Hmarcha 🌶️", "Mircha 🌶️"),
                        getLocalizedText(lang, "Screwdriver 🪛", "স্ক্ৰু-ড্ৰাইভাৰ 🪛", "पेचकस 🪛", "স্ক্রু-ড্রাইভার 🪛", "স্ক্রু ড্রাইভর 🪛", "Screwdriver 🪛", "Hmanrua 🪛", "Screwdriver 🪛")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ro_2_2",
                    promptKey = getLocalizedText(lang, "Which North East regional flower was shown?", "উত্তৰ-পূবৰ কোনটো আঞ্চলিক ফুল দেখুওৱা হৈছিল?", "पूर्वोत्तर का कौन सा क्षेत्रीय फूल दिखाया गया था?", "উত্তর-পূর্বের কোন আঞ্চলিক ফুলটি দেখানো হয়েছিল?", "অৱাং-নোংপোক্কী করম্বা লৈ দেখহন্বগে?", "Kano ka tiew kaba pawnam ha North East ba la pyni?", "North East pangpar mawi tak eng nge an tarlan kha?", "North East laga kun phool dekhishey?"),
                    promptTextFallback = getLocalizedText(lang, "Which North East regional flower was shown?", "উত্তৰ-পূবৰ কোনটো আঞ্চলিক ফুল দেখুওৱা হৈছিল?", "पूर्वोत्तर का कौन सा क्षेत्रीय फूल दिखाया गया था?", "উত্তর-পূর্বের কোন আঞ্চলিক ফুলটি দেখানো হয়েছিল?", "অৱাং-নোংপোক্কী করম্বা লৈ দেখহন্বগে?", "Kano ka tiew kaba pawnam ha North East ba la pyni?", "North East pangpar mawi tak eng nge an tarlan kha?", "North East laga kun phool dekhishey?"),
                    visualEmoji = "🌸",
                    options = listOf(
                        getLocalizedText(lang, "Pink Orchid 🌸", "গুলপীয়া অৰ্কিড 🌸", "गुलाबी ऑर्किड 🌸", "গোলাপি অর্কিড 🌸", "অকোম্পা ওর্কিদ 🌸", "Tiew orchid saw 🌸", "Orchid pangpar 🌸", "Golapi Orchid 🌸"),
                        getLocalizedText(lang, "Cactus 🌵", "কেকটাছ 🌵", "कैक्टस 🌵", "ক্যাকটাস 🌵", "কেক্তাস 🌵", "Cactus 🌵", "Cactus 🌵", "Cactus 🌵"),
                        getLocalizedText(lang, "Pinecone 🌲", "পাইনকন 🌲", "चीड़ का शंकु 🌲", "পাইন পাইন 🌲", "পাইন 🌲", "Sohkymphor 🌲", "Far 🌲", "Pine 🌲")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ro_2_3",
                    promptKey = getLocalizedText(lang, "Which item keeps us warm in chilly mountain weather?", "শীতল পাহাৰীয়া বতৰত আমাক কোনটো বস্তুই উমাল কৰি ৰাখে?", "पहाड़ी ठंड में कौन सी वस्तु हमें गर्म रखती है?", "পাহাড়ি ঠান্ডায় কোন জিনিসটি আমাদের উষ্ণ রাখে?", "চীংগী ইংবা মতমদা ঐখোয়বু করম্বা পোৎলমনা শাফহন্বগে?", "Kano ka tiar kaba pynsyaid ia ngi ha ka por tlang ha lum?", "Tlang vawt laia min ti lumtu ber thil eng nge?", "Thanda jaga te kun kapor garm rakhe?"),
                    promptTextFallback = getLocalizedText(lang, "Which item keeps us warm in chilly mountain weather?", "শীতল পাহাৰীয়া বতৰত আমাক কোনটো বস্তুই উমাল কৰি ৰাখে?", "पहाड़ी ठंड में कौन सी वस्तु हमें गर्म रखती है?", "পাহাড়ি ঠান্ডায় কোন জিনিসটি আমাদের উষ্ণ রাখে?", "চীংগী ইংবা মতমদা ঐখোয়বু করম্বা পোৎলমনা শাফহন্বগে?", "Kano ka tiar kaba pynsyaid ia ngi ha ka por tlang ha lum?", "Tlang vawt laia min ti lumtu ber thil eng nge?", "Thanda jaga te kun kapor garm rakhe?"),
                    visualEmoji = "🧣",
                    options = listOf(
                        getLocalizedText(lang, "Warm Shawl / Scarf 🧣", "উমাল চাদৰ / মাফলাৰ 🧣", "गर्म शॉल या मफलर 🧣", "উষ্ণ চাদর বা মাফলার 🧣", "শাফবা ফি 🧣", "Jainkup syaid 🧣", "Puan lum 🧣", "Garm Shawl 🧣"),
                        getLocalizedText(lang, "Swimsuit 🩱", "সাঁতোৰা কাপোৰ 🩱", "तैराकी पोशाक 🩱", "সাঁতারের পোশাক 🩱", "ইরুজবা ফি 🩱", "Jain jngi 🩱", "Inbual kawr 🩱", "Swimsuit 🩱"),
                        getLocalizedText(lang, "Plastic Spoon 🥄", "প্লাষ্টিকৰ চামুচ 🥄", "प्लास्टिक का चम्मच 🥄", "প্লাস্টিক চামচ 🥄", "চামোচ 🥄", "Spoon plastic 🥄", "Thirfiante 🥄", "Chamach 🥄")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "ro_3_1",
                    promptKey = getLocalizedText(lang, "Select the object that appeared on your screen:", "পৰ্দাত প্ৰদৰ্শিত হোৱা বস্তুটো বাচক:", "स्क्रीन पर दिखाई गई वस्तु को चुनें:", "স্ক্রিনে দেখানো বস্তুটি নির্বাচন করুন:", "স্ক্রিনদা উখিবা পোৎলম অদু খল্লু:", "Jied ia ka tiar kaba la paw ha ka screen:", "Screen-a lo lang kha thlang chhuak rawh:", "Screen te dekha saman toh chunibo:"),
                    promptTextFallback = getLocalizedText(lang, "Select the object that appeared on your screen:", "পৰ্দাত প্ৰদৰ্শিত হোৱা বস্তুটো বাচক:", "स्क्रीन पर दिखाई गई वस्तु को चुनें:", "স্ক্রিনে দেখানো বস্তুটি নির্বাচন করুন:", "স্ক্রিনদা উখিবা পোৎলম অদু খল্লু:", "Jied ia ka tiar kaba la paw ha ka screen:", "Screen-a lo lang kha thlang chhuak rawh:", "Screen te dekha saman toh chunibo:"),
                    visualEmoji = "🧺",
                    options = listOf(
                        getLocalizedText(lang, "Bamboo Basket 🧺", "বাঁহৰ খৰাহি 🧺", "बांस की टोकरी 🧺", "বাঁশের ঝুড়ি 🧺", "ৱাথৌ 🧺", "Kriah siej 🧺", "Thlangra 🧺", "Bans Basket 🧺"),
                        getLocalizedText(lang, "Ice Cream 🍦", "আইচক্ৰীম 🍦", "आइसक्रीम 🍦", "আইসক্রিম 🍦", "আইসক্রিম 🍦", "Ice Cream 🍦", "Ais-krim 🍦", "Ice Cream 🍦"),
                        getLocalizedText(lang, "Airplane ✈️", "উৰাজাহাজ ✈️", "हवाई जहाज ✈️", "উড়োজাহাজ ✈️", "মৈথাং প্লেন ✈️", "Liengsuidhan ✈️", "Thlawhna ✈️", "Jahaj ✈️"),
                        getLocalizedText(lang, "Bicycle 🚲", "চাইকেল 🚲", "साइकिल 🚲", "সাইকেল 🚲", "সাইকল 🚲", "Saitkul 🚲", "Thir sakawr 🚲", "Cycle 🚲")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ro_3_2",
                    promptKey = getLocalizedText(lang, "Which morning reading item was in the collection?", "সংগ্ৰহটোত ৰাতিপুৱাৰ পঢ়া কোনটো বস্তু আছিল?", "संग्रह में सुबह पढ़ने की कौन सी वस्तु थी?", "সকালে পড়ার কোন জিনিসটি তালিকায় ছিল?", "অয়ুক্কী পাবা পোৎলমশিংদা করি য়াওখিবগে?", "Kano ka tiar pule mynstep kaba don ha ka?", "Zing lama chhiar chi eng nge an chhawp chhuah kha?", "Phujor porha saman kun asey?"),
                    promptTextFallback = getLocalizedText(lang, "Which morning reading item was in the collection?", "সংগ্ৰহটোত ৰাতিপুৱাৰ পঢ়া কোনটো বস্তু আছিল?", "संग्रह में सुबह पढ़ने की कौन सी वस्तु थी?", "সকালে পড়ার কোন জিনিসটি তালিকায় ছিল?", "অয়ুক্কী পাবা পোৎলমশিংদা করি য়াওখিবগে?", "Kano ka tiar pule mynstep kaba don ha ka?", "Zing lama chhiar chi eng nge an chhawp chhuah kha?", "Phujor porha saman kun asey?"),
                    visualEmoji = "📰",
                    options = listOf(
                        getLocalizedText(lang, "Newspaper 📰", "বাতৰি কাকত 📰", "समाचार पत्र 📰", "সংবাদপত্র 📰", "চেফোং 📰", "Kotkhubor 📰", "Chanchinbu 📰", "Khabar Kagaz 📰"),
                        getLocalizedText(lang, "Hammer 🔨", "হাতুৰী 🔨", "हथौड़ा 🔨", "হাতুড়ি 🔨", "থাংগোই 🔨", "Muti 🔨", "Tuthlaw 🔨", "Haturi 🔨"),
                        getLocalizedText(lang, "Torch 🔦", "টৰ্চলাইট 🔦", "टॉर्च 🔦", "টর্চ 🔦", "তোর্চ 🔦", "Sharak 🔦", "Meichher 🔦", "Torch 🔦"),
                        getLocalizedText(lang, "Tennis Racket 🎾", "ৰেকেট 🎾", "टेनिस रैकेट 🎾", "র‌্যাকেট 🎾", "রেকেত 🎾", "Racket 🎾", "Tualchhung 🎾", "Racket 🎾")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ro_3_3",
                    promptKey = getLocalizedText(lang, "Which bird of North East was in the memory cards?", "স্মৃতি কাৰ্ডত উত্তৰ-পূবৰ কোনটো চৰাই আছিল?", "मेमोरी कार्ड्स में पूर्वोत्तर का कौन सा पक्षी था?", "মেমোরি কার্ডে উত্তর-পূর্বের কোন পাখিটি ছিল?", "নীংশিং কার্ডশিংদা অৱাং-নোংপোক্কী করম্বা উচেক য়াওবগে?", "Uba kumno u sim jong ka North East uba don ha ki card?", "North East sava hmingthang tak eng nge card-ah khan awm?", "North East laga kun chora card te thakishey?"),
                    promptTextFallback = getLocalizedText(lang, "Which bird of North East was in the memory cards?", "স্মৃতি কাৰ্ডত উত্তৰ-পূবৰ কোনটো চৰাই আছিল?", "मेमोरी कार्ड्स में पूर्वोत्तर का कौन सा पक्षी था?", "মেমোরি কার্ডে উত্তর-পূর্বের কোন পাখিটি ছিল?", "নীংশিং কার্ডশিংদা অৱাং-নোংপোক্কী করম্বা উচেক য়াওবগে?", "Uba kumno u sim jong ka North East uba don ha ki card?", "North East sava hmingthang tak eng nge card-ah khan awm?", "North East laga kun chora card te thakishey?"),
                    visualEmoji = "🦜",
                    options = listOf(
                        getLocalizedText(lang, "Great Hornbill 🦜", "ধনেশ পক্ষী 🦜", "ग्रेट हॉर्नबिल 🦜", "ধনেশ পাখি 🦜", "উচেক উরোং 🦜", "Sim Kohhai 🦜", "Vapual 🦜", "Hornbill Chora 🦜"),
                        getLocalizedText(lang, "Penguin 🐧", "পেংগুইন 🐧", "पेंगुइन 🐧", "পেঙ্গুইন 🐧", "পেঙ্গুইন 🐧", "Penguin 🐧", "Penguin 🐧", "Penguin 🐧"),
                        getLocalizedText(lang, "Ostrich 🦤", "উটপখী 🦤", "शुतुरमुर्ग 🦤", "উটপাখি 🦤", "ওস্ত্রিচ 🦤", "Ostrich 🦤", "Savapui 🦤", "Ostrich 🦤"),
                        getLocalizedText(lang, "Flamingo 🦩", "ৰাজহাঁহ 🦩", "राजहंस 🦩", "ফ্ল্যামিঙ্গো 🦩", "ফ্লেমিঙ্গো 🦩", "Flamingo 🦩", "Flamingo 🦩", "Flamingo 🦩")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generateFindTargetQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "ft_1_1",
                    promptKey = getLocalizedText(lang, "Touch the bright shining Sun ☀️:", "উজ্বলকৈ জিলিকি থকা সূৰ্য্যটো স্পৰ্শ কৰক ☀️:", "चमकते हुए सूरज को छुएं ☀️:", "উজ্জ্বল জ্বলজ্বলে সূর্যটি স্পর্শ করুন ☀️:", "ঙাল্লিবা নুমিৎ অদু থমবীয়ু ☀️:", "Khyllie ia ka sngi ba tyngshain ☀️:", "Ni eng tak saw khawih rawh ☀️:", "Chomok kora Suraj ke chubi low ☀️:"),
                    promptTextFallback = getLocalizedText(lang, "Touch the bright shining Sun ☀️:", "উজ্বলকৈ জিলিকি থকা সূৰ্য্যটো স্পৰ্শ কৰক ☀️:", "चमकते हुए सूरज को छुएं ☀️:", "উজ্জ্বল জ্বলজ্বলে সূর্যটি স্পর্শ করুন ☀️:", "ঙাল্লিবা নুমিৎ অদু থমবীয়ু ☀️:", "Khyllie ia ka sngi ba tyngshain ☀️:", "Ni eng tak saw khawih rawh ☀️:", "Chomok kora Suraj ke chubi low ☀️:"),
                    visualEmoji = "☀️",
                    options = listOf(
                        getLocalizedText(lang, "☀️ Sun", "☀️ সূৰ্য্য", "☀️ सूरज", "☀️ সূর্য", "☀️ নুমিৎ", "☀️ Ka sngi", "☀️ Ni", "☀️ Suraj"),
                        getLocalizedText(lang, "🌧️ Rain", "🌧️ বৰষুণ", "🌧️ बारिश", "🌧️ বৃষ্টি", "🌧️ নোং", "🌧️ U slap", "🌧️ Ruah", "🌧️ Boroxun")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ft_1_2",
                    promptKey = getLocalizedText(lang, "Find the fresh Green Leaf 🌿:", "সতেজ সেউজীয়া পাত বিচাৰি উলিয়াওক 🌿:", "ताज़ी हरी पत्ती खोजें 🌿:", "সতেজ সবুজ পাতা খুঁজুন 🌿:", "অশংবা মনা অদু থিবীয়ু 🌿:", "Wad ia ka sla jyrngam 🌿:", "Hnah hring mawi tak saw zawng chhuak rawh 🌿:", "Taza Hori Paata bisari low 🌿:"),
                    promptTextFallback = getLocalizedText(lang, "Find the fresh Green Leaf 🌿:", "সতেজ সেউজীয়া পাত বিচাৰি উলিয়াওক 🌿:", "ताज़ी हरी पत्ती खोजें 🌿:", "সতেজ সবুজ পাতা খুঁজুন 🌿:", "অশংবা মনা অদু থিবীয়ু 🌿:", "Wad ia ka sla jyrngam 🌿:", "Hnah hring mawi tak saw zawng chhuak rawh 🌿:", "Taza Hori Paata bisari low 🌿:"),
                    visualEmoji = "🌿",
                    options = listOf(
                        getLocalizedText(lang, "🌿 Leaf", "🌿 পাত", "🌿 पत्ती", "🌿 পাতা", "🌿 মনা", "🌿 Ka sla", "🌿 Hnah", "🌿 Paata"),
                        getLocalizedText(lang, "🔥 Flame", "🔥 জুই", "🔥 आग", "🔥 আগুন", "🔥 মৈ", "🔥 Ka ding", "🔥 Mei", "🔥 Jui")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "ft_2_1",
                    promptKey = getLocalizedText(lang, "Spot the majestic Rhinoceros 🦏:", "একশিঙীয়া গঁড়টো বাচক 🦏:", "भव्य गैंडे को पहचानें 🦏:", "একশৃঙ্গ গণ্ডারটি চিহ্নিত করুন 🦏:", "কাজিরঙ্গাগী সমুক অদু খল্লু 🦏:", "Wad ia u kynda (Rhino) 🦏:", "Samak pui saw zawng chhuak rawh 🦏:", "Kaziranga Rhino ke bisari low 🦏:"),
                    promptTextFallback = getLocalizedText(lang, "Spot the majestic Rhinoceros 🦏:", "একশিঙীয়া গঁড়টো বাচক 🦏:", "भव्य गैंडे को पहचानें 🦏:", "একশৃঙ্গ গণ্ডারটি চিহ্নিত করুন 🦏:", "কাজিরঙ্গাগী সমুক অদু খল্লু 🦏:", "Wad ia u kynda (Rhino) 🦏:", "Samak pui saw zawng chhuak rawh 🦏:", "Kaziranga Rhino ke bisari low 🦏:"),
                    visualEmoji = "🦏",
                    options = listOf(
                        getLocalizedText(lang, "🦏 Rhino", "🦏 গঁড়", "🦏 गैंडा", "🦏 গণ্ডার", "🦏 সমুক", "🦏 Kynda", "🦏 Samak", "🦏 Rhino"),
                        getLocalizedText(lang, "🐘 Elephant", "🐘 হাতী", "🐘 हाथी", "🐘 হাতি", "🐘 শমু", "🐘 Hati", "🐘 Sai", "🐘 Haathi"),
                        getLocalizedText(lang, "🐅 Tiger", "🐅 বাঘ", "🐅 बाघ", "🐅 বাঘ", "🐅 কেই", "🐅 Khla", "🐅 Sakei", "🐅 Bagh")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ft_2_2",
                    promptKey = getLocalizedText(lang, "Find the warm cup of Assam Tea 🍵:", "অসমৰ গৰম চাহৰ কাপটো বিচাৰক 🍵:", "असम चाय का गर्म कप खोजें 🍵:", "আসামের গরম চায়ের কাপটি খুঁজুন 🍵:", "অসামগী শাফবা চা কপ থিবীয়ু 🍵:", "Wad ia ka khiew sha Assam 🍵:", "Assam thingpui lum no saw zawng chhuak rawh 🍵:", "Assam laga garam Cha Cup bisari low 🍵:"),
                    promptTextFallback = getLocalizedText(lang, "Find the warm cup of Assam Tea 🍵:", "অসমৰ গৰম চাহৰ কাপটো বিচাৰক 🍵:", "असम चाय का गर्म कप खोजें 🍵:", "আসামের গরম চায়ের কাপটি খুঁজুন 🍵:", "অসামগী শাফবা চা কপ থিবীয়ু 🍵:", "Wad ia ka khiew sha Assam 🍵:", "Assam thingpui lum no saw zawng chhuak rawh 🍵:", "Assam laga garam Cha Cup bisari low 🍵:"),
                    visualEmoji = "🍵",
                    options = listOf(
                        getLocalizedText(lang, "🍵 Tea Cup", "🍵 চাহৰ কাপ", "🍵 चाय का कप", "🍵 চায়ের কাপ", "🍵 চা কপ", "🍵 Khiew sha", "🍵 Thingpui no", "🍵 Cha Cup"),
                        getLocalizedText(lang, "🥤 Cold Soda", "🥤 শীতল চ'ডা", "🥤 ठंडा सोडा", "🥤 ঠান্ডা সোডা", "🥤 কোল্ড দ্রিন্ক", "🥤 Umphniang", "🥤 Tui vawt", "🥤 Cold Soda"),
                        getLocalizedText(lang, "🍼 Baby Bottle", "🍼 ফিডিং বটল", "🍼 दूध की बोतल", "🍼 দুধের বোতল", "🍼 শঙ্গোম বোতল", "🍼 Botol dud", "🍼 Hnute bawm", "🍼 Dudh Bottle")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ft_2_3",
                    promptKey = getLocalizedText(lang, "Spot the musical Bell 🔔:", "ঘণ্টাটো বাচক 🔔:", "संगीत की घंटी पहचानें 🔔:", "বাজনা ঘণ্টাটি নির্বাচন করুন 🔔:", "ঘন্তা অদু খল্লু 🔔:", "Wad ia ka shakuria 🔔:", "Dar ri mawi tak saw zawng chhuak rawh 🔔:", "Ghanti ke bisari low 🔔:"),
                    promptTextFallback = getLocalizedText(lang, "Spot the musical Bell 🔔:", "ঘণ্টাটো বাচক 🔔:", "संगीत की घंटी पहचानें 🔔:", "বাজনা ঘণ্টাটি নির্বাচন করুন 🔔:", "ঘন্তা অদু খল্লু 🔔:", "Wad ia ka shakuria 🔔:", "Dar ri mawi tak saw zawng chhuak rawh 🔔:", "Ghanti ke bisari low 🔔:"),
                    visualEmoji = "🔔",
                    options = listOf(
                        getLocalizedText(lang, "🔔 Bell", "🔔 ঘণ্টা", "🔔 घंटी", "🔔 ঘণ্টা", "🔔 ঘন্তা", "🔔 Shakuria", "🔔 Dar", "🔔 Ghanti"),
                        getLocalizedText(lang, "🥁 Drum", "🥁 ঢোল", "🥁 ढोल", "🥁 ঢোল", "🥁 পুং", "🥁 Ksing", "🥁 Khuallam", "🥁 Dhol"),
                        getLocalizedText(lang, "🎺 Horn", "🎺 পেঁপা", "🎺 भोंपू", "🎺 শিঙা", "🎺 পেঁপা", "🎺 Ronsing", "🎺 Tawtawrawt", "🎺 Pepa")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "ft_3_1",
                    promptKey = getLocalizedText(lang, "Find the Red Apple among the fruits:", "ফলবোৰৰ মাজৰ পৰা ৰঙা আপেলটো বাচক:", "फलों के बीच लाल सेब को पहचानें:", "ফলগুলির মধ্য থেকে লাল আপেলটি বেছে নিন:", "হৈশিংগী মরক্তগী অঙাংবা সেও খল্লু:", "Wad ia u soh saw ha pdeng ki soh:", "Thei zingah Apple sen saw zawng chhuak rawh:", "Phal majete Lal Apple ke bisari low:"),
                    promptTextFallback = getLocalizedText(lang, "Find the Red Apple among the fruits:", "ফলবোৰৰ মাজৰ পৰা ৰঙা আপেলটো বাচক:", "फलों के बीच लाल सेब को पहचानें:", "ফলগুলির মধ্য থেকে লাল আপেলটি বেছে নিন:", "হৈশিংগী মরক্তগী অঙাংবা সেও খল্লু:", "Wad ia u soh saw ha pdeng ki soh:", "Thei zingah Apple sen saw zawng chhuak rawh:", "Phal majete Lal Apple ke bisari low:"),
                    visualEmoji = "🍎",
                    options = listOf(
                        getLocalizedText(lang, "🍎 Apple", "🍎 আপেল", "🍎 सेब", "🍎 আপেল", "🍎 সেও", "🍎 Soh saw", "🍎 Apple", "🍎 Apple"),
                        getLocalizedText(lang, "🍌 Banana", "🍌 কল", "🍌 केला", "🍌 কলা", "🍌 লাফোই", "🍌 Kait", "🍌 Balhla", "🍌 Kela"),
                        getLocalizedText(lang, "🍇 Grapes", "🍇 আঙুৰ", "🍇 अंगूर", "🍇 আঙুর", "🍇 অঙ্গুর", "🍇 Soh drakha", "🍇 Grep", "🍇 Angoor"),
                        getLocalizedText(lang, "🍊 Orange", "🍊 কমলা", "🍊 संतरा", "🍊 কমলা", "🍊 কোমলা", "🍊 Sohniamtra", "🍊 Serthlum", "🍊 Komola")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ft_3_2",
                    promptKey = getLocalizedText(lang, "Find the Hornbill bird 🦜 among these animals:", "এই জীৱ-জন্তুবোৰৰ মাজৰ পৰা ধনেশ পক্ষী 🦜 বিচাৰক:", "इनमें से हॉर्नबिल पक्षी 🦜 को पहचानें:", "এই পশু-পাখিদের মধ্য থেকে ধনেশ পাখি 🦜 চিহ্নিত করুন:", "উচেক উরোং 🦜 অদু খল্লু:", "Wad ia u sim Hornbill 🦜 ha pdeng kine ki mrad:", "Nungcha zingah Vapual sava 🦜 saw zawng chhuak rawh:", "Hornbill chora 🦜 ke bisari low:"),
                    promptTextFallback = getLocalizedText(lang, "Find the Hornbill bird 🦜 among these animals:", "এই জীৱ-জন্তুবোৰৰ মাজৰ পৰা ধনেশ পক্ষী 🦜 বিচাৰক:", "इनमें से हॉर्नबिल पक्षी 🦜 को पहचानें:", "এই পশু-পাখিদের মধ্য থেকে ধনেশ পাখি 🦜 চিহ্নিত করুন:", "উচেক উরোং 🦜 অদু খল্লু:", "Wad ia u sim Hornbill 🦜 ha pdeng kine ki mrad:", "Nungcha zingah Vapual sava 🦜 saw zawng chhuak rawh:", "Hornbill chora 🦜 ke bisari low:"),
                    visualEmoji = "🦜",
                    options = listOf(
                        getLocalizedText(lang, "🦜 Hornbill", "🦜 ধনেশ পক্ষী", "🦜 हॉर्नबिल", "🦜 ধনেশ পাখি", "🦜 উচেক উরোং", "🦜 Hornbill", "🦜 Vapual", "🦜 Hornbill"),
                        getLocalizedText(lang, "🦚 Peacock", "🦚 ময়ূৰ", "🦚 मोर", "🦚 ময়ূর", "🦚 ৱাহং", "🦚 Peacock", "🦚 A-ka", "🦚 Mor"),
                        getLocalizedText(lang, "🦉 Owl", "🦉 ফেঁচা", "🦉 उल्लू", "🦉 প্যাঁচা", "🦉 উচুক", "🦉 Dkhoh", "🦉 Ching-ching", "🦉 Ulloo"),
                        getLocalizedText(lang, "🦆 Duck", "🦆 হাঁহ", "🦆 बत्तख", "🦆 হাঁস", "🦆 ঙানু", "🦆 Hanse", "🦆 Varak", "🦆 Haah")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ft_3_3",
                    promptKey = getLocalizedText(lang, "Identify the clock showing day time ⏰:", "দিনৰ সময় দেখুওৱা ঘড়ীটো বাচক ⏰:", "समय बताने वाली घड़ी को पहचानें ⏰:", "দিনের সময় দেখানো ঘড়িটি চিহ্নিত করুন ⏰:", "মতম তাক্লিবা ঘড়ী অদু খল্লু ⏰:", "Wad ia ka baje ⏰:", "Hun entirtu sana saw zawng chhuak rawh ⏰:", "Time dekhua ghori ke chunibo ⏰:"),
                    promptTextFallback = getLocalizedText(lang, "Identify the clock showing day time ⏰:", "দিনৰ সময় দেখুওৱা ঘড়ীটো বাচক ⏰:", "समय बताने वाली घड़ी को पहचानें ⏰:", "দিনের সময় দেখানো ঘড়িটি চিহ্নিত করুন ⏰:", "মতম তাক্লিবা ঘড়ী অদু খল্লু ⏰:", "Wad ia ka baje ⏰:", "Hun entirtu sana saw zawng chhuak rawh ⏰:", "Time dekhua ghori ke chunibo ⏰:"),
                    visualEmoji = "⏰",
                    options = listOf(
                        getLocalizedText(lang, "⏰ Alarm Clock", "⏰ এলাৰ্ম ঘড়ী", "⏰ अलार्म घड़ी", "⏰ অ্যালার্ম ঘড়ি", "⏰ ঘড়ী", "⏰ Baje", "⏰ Sana", "⏰ Ghori"),
                        getLocalizedText(lang, "📱 Mobile Screen", "📱 মোবাইলৰ পৰ্দা", "📱 मोबाइल स्क्रीन", "📱 মোবাইল পর্দা", "📱 মোবাইল", "📱 Mobile", "📱 Phone", "📱 Mobile"),
                        getLocalizedText(lang, "🔦 Flashlight", "🔦 টৰ্চলাইট", "🔦 टॉर्च", "🔦 টর্চলাইট", "🔦 তোর্চ", "🔦 Sharak", "🔦 Meichher", "🔦 Flashlight"),
                        getLocalizedText(lang, "📻 Radio", "📻 ৰেডিঅ'", "📻 रेडियो", "📻 রেডিও", "📻 রেদিও", "📻 Radio", "📻 Radio", "📻 Radio")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generateSequenceAttentionQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "sa_1_1",
                    promptKey = getLocalizedText(lang, "What comes next in this line? 🔴 🔵 🔴 ❓", "ক্ৰমত পিছত কি আহিব? 🔴 🔵 🔴 ❓", "इस पंक्ति में अगला क्या आएगा? 🔴 🔵 🔴 ❓", "এই সারিতে পরবর্তীতে কী আসবে? 🔴 🔵 🔴 ❓", "পরিংসিদা মথংদা করি লাক্কনি? 🔴 🔵 🔴 ❓", "Aiu ban wan bud ha kane? 🔴 🔵 🔴 ❓", "Eng nge a dawtah awm ang? 🔴 🔵 🔴 ❓", "Next te ki ahibo? 🔴 🔵 🔴 ❓"),
                    promptTextFallback = getLocalizedText(lang, "What comes next in this line? 🔴 🔵 🔴 ❓", "ক্ৰমত পিছত কি আহিব? 🔴 🔵 🔴 ❓", "इस पंक्ति में अगला क्या आएगा? 🔴 🔵 🔴 ❓", "এই সারিতে পরবর্তীতে কী আসবে? 🔴 🔵 🔴 ❓", "পরিংসিদা মথংদা করি লাক্কনি? 🔴 🔵 🔴 ❓", "Aiu ban wan bud ha kane? 🔴 🔵 🔴 ❓", "Eng nge a dawtah awm ang? 🔴 🔵 🔴 ❓", "Next te ki ahibo? 🔴 🔵 🔴 ❓"),
                    visualEmoji = "🔴 🔵 🔴 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🔵 Blue Circle", "🔵 নীলা বৃত্ত", "🔵 नीला घेरा", "🔵 নীল বৃত্ত", "🔵 হিগোক গোল", "🔵 Circle jylliew", "🔵 Hring pawl", "🔵 Neela Gura"),
                        getLocalizedText(lang, "🟢 Green Circle", "🟢 সেউজীয়া বৃত্ত", "🟢 हरा घेरा", "🟢 সবুজ বৃত্ত", "🟢 অশংবা গোল", "🟢 Circle jyrngam", "🟢 Hring hring", "🟢 Hori Gura")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sa_1_2",
                    promptKey = getLocalizedText(lang, "What number follows? 1, 2, 3, ❓", "কোনটো সংখ্যা পিছত বহিব? ১, ২, ৩, ❓", "अगली संख्या कौन सी है? 1, 2, 3, ❓", "পরের সংখ্যাটি কী? ১, ২, ৩, ❓", "মথংদা করম্বা মশীং লাক্কনি? ১, ২, ৩, ❓", "Uba kumno u number ban bud? 1, 2, 3, ❓", "Eng number nge dawt ang? 1, 2, 3, ❓", "Next number ki ahibo? 1, 2, 3, ❓"),
                    promptTextFallback = getLocalizedText(lang, "What number follows? 1, 2, 3, ❓", "কোনটো সংখ্যা পিছত বহিব? ১, ২, ৩, ❓", "अगली संख्या कौन सी है? 1, 2, 3, ❓", "পরের সংখ্যাটি কী? ১, ২, ৩, ❓", "মথংদা করম্বা মশীং লাক্কনি? ১, ২, ৩, ❓", "Uba kumno u number ban bud? 1, 2, 3, ❓", "Eng number nge dawt ang? 1, 2, 3, ❓", "Next number ki ahibo? 1, 2, 3, ❓"),
                    visualEmoji = "1️⃣ 2️⃣ 3️⃣ ❓",
                    options = listOf(
                        getLocalizedText(lang, "4️⃣ Four", "4️⃣ চাৰি", "4️⃣ चार", "4️⃣ চার", "4️⃣ মরি", "4️⃣ Saw", "4️⃣ Pali", "4️⃣ Chaar"),
                        getLocalizedText(lang, "1️⃣ One", "1️⃣ এক", "1️⃣ एक", "1️⃣ এক", "1️⃣ অমা", "1️⃣ Wei", "1️⃣ Pakhat", "1️⃣ Ek")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "sa_2_1",
                    promptKey = getLocalizedText(lang, "Which symbol completes the sequence? ⭐ 🌙 ⭐ 🌙 ❓", "ক্ৰমটো সম্পূৰ্ণ কৰিবলৈ কোনটো চিহ্ন বহিব? ⭐ 🌙 ⭐ 🌙 ❓", "इस अनुक्रम को पूरा करने वाला प्रतीक कौन सा है? ⭐ 🌙 ⭐ 🌙 ❓", "কোন প্রতীকটি অনুক্রমটি সম্পূর্ণ করবে? ⭐ 🌙 ⭐ 🌙 ❓", "পরিংসি লোইশিনগদবা মশক অদু করিনো? ⭐ 🌙 ⭐ 🌙 ❓", "Kano ka dak ban pynbiang? ⭐ 🌙 ⭐ 🌙 ❓", "A dawtah eng lem nge awm ang? ⭐ 🌙 ⭐ 🌙 ❓", "Next sign ki ahibo? ⭐ 🌙 ⭐ 🌙 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Which symbol completes the sequence? ⭐ 🌙 ⭐ 🌙 ❓", "ক্ৰমটো সম্পূৰ্ণ কৰিবলৈ কোনটো চিহ্ন বহিব? ⭐ 🌙 ⭐ 🌙 ❓", "इस अनुक्रम को पूरा करने वाला प्रतीक कौन सा है? ⭐ 🌙 ⭐ 🌙 ❓", "কোন প্রতীকটি অনুক্রমটি সম্পূর্ণ করবে? ⭐ 🌙 ⭐ 🌙 ❓", "পরিংসি লোইশিনগদবা মশক অদু করিনো? ⭐ 🌙 ⭐ 🌙 ❓", "Kano ka dak ban pynbiang? ⭐ 🌙 ⭐ 🌙 ❓", "A dawtah eng lem nge awm ang? ⭐ 🌙 ⭐ 🌙 ❓", "Next sign ki ahibo? ⭐ 🌙 ⭐ 🌙 ❓"),
                    visualEmoji = "⭐ 🌙 ⭐ 🌙 ❓",
                    options = listOf(
                        getLocalizedText(lang, "⭐ Star", "⭐ তৰা", "⭐ तारा", "⭐ তারা", "⭐ থৱানমিচাক", "⭐ Khlur", "⭐ Arsi", "⭐ Tara"),
                        getLocalizedText(lang, "☀️ Sun", "☀️ সূৰ্য্য", "☀️ सूरज", "☀️ সূর্য", "☀️ নুমিৎ", "☀️ Sngi", "☀️ Ni", "☀️ Suraj"),
                        getLocalizedText(lang, "☁️ Cloud", "☁️ ডাৱৰ", "☁️ बादल", "☁️ মেঘ", "☁️ লৈচিল", "☁️ Lyoh", "☁️ Chhum", "☁️ Badal")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sa_2_2",
                    promptKey = getLocalizedText(lang, "Counting upwards by twos: 2, 4, 6, ❓", "দুটাকৈ আগবঢ়া গণনা: ২, ৪, ৬, ❓", "दो-दो की गिनती: 2, 4, 6, ❓", "দুই করে গণনা: ২, ৪, ৬, ❓", "অনী অনী হাপচিন্দুনা মশীং থিবা: ২, ৪, ৬, ❓", "Khein da ki ar: 2, 4, 6, ❓", "Hnih zela chhiarin: 2, 4, 6, ❓", "Dui dui ginti: 2, 4, 6, ❓"),
                    promptTextFallback = getLocalizedText(lang, "Counting upwards by twos: 2, 4, 6, ❓", "দুটাকৈ আগবঢ়া গণনা: ২, ৪, ৬, ❓", "दो-दो की गिनती: 2, 4, 6, ❓", "দুই করে গণনা: ২, ৪, ৬, ❓", "অনী অনী হাপচিন্দুনা মশীং থিবা: ২, ৪, ৬, ❓", "Khein da ki ar: 2, 4, 6, ❓", "Hnih zela chhiarin: 2, 4, 6, ❓", "Dui dui ginti: 2, 4, 6, ❓"),
                    visualEmoji = "2 ➔ 4 ➔ 6 ➔ ❓",
                    options = listOf("8", "7", "3"),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sa_2_3",
                    promptKey = getLocalizedText(lang, "Shape rhythm: 🔺 ⬛ 🔺 ⬛ ❓", "আকৃতিৰ ছন্দ: 🔺 ⬛ 🔺 ⬛ ❓", "आकृति क्रम: 🔺 ⬛ 🔺 ⬛ ❓", "আকৃতির ছন্দ: 🔺 ⬛ 🔺 ⬛ ❓", "মশক পরিং: 🔺 ⬛ 🔺 ⬛ ❓", "Dur ba bud: 🔺 ⬛ 🔺 ⬛ ❓", "A dawtah eng nge awm ang: 🔺 ⬛ 🔺 ⬛ ❓", "Shape milaibo: 🔺 ⬛ 🔺 ⬛ ❓"),
                    promptTextFallback = getLocalizedText(lang, "Shape rhythm: 🔺 ⬛ 🔺 ⬛ ❓", "আকৃতিৰ ছন্দ: 🔺 ⬛ 🔺 ⬛ ❓", "आकृति क्रम: 🔺 ⬛ 🔺 ⬛ ❓", "আকৃতির ছন্দ: 🔺 ⬛ 🔺 ⬛ ❓", "মশক পরিং: 🔺 ⬛ 🔺 ⬛ ❓", "Dur ba bud: 🔺 ⬛ 🔺 ⬛ ❓", "A dawtah eng nge awm ang: 🔺 ⬛ 🔺 ⬛ ❓", "Shape milaibo: 🔺 ⬛ 🔺 ⬛ ❓"),
                    visualEmoji = "🔺 ⬛ 🔺 ⬛ ❓",
                    options = listOf(
                        getLocalizedText(lang, "🔺 Triangle", "🔺 ত্ৰিভুজ", "🔺 त्रिभुज", "🔺 ত্রিভুজ", "🔺 ত্রিভুজ", "🔺 Triangle", "🔺 Triangle", "🔺 Triangle"),
                        getLocalizedText(lang, "⚪ Circle", "⚪ বৃত্ত", "⚪ गोला", "⚪ বৃত্ত", "⚪ গোল", "⚪ Circle", "⚪ Bial", "⚪ Gura"),
                        getLocalizedText(lang, "🔷 Diamond", "🔷 ৰম্বাছ", "🔷 हीरा आकार", "🔷 রম্বস", "🔷 হীরা", "🔷 Diamond", "🔷 Diamond", "🔷 Diamond")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "sa_3_1",
                    promptKey = getLocalizedText(lang, "Notice the repeating trio: 🍎 🍌 🍇 🍎 🍌 ❓", "পুনৰাবৃত্ত ক্ৰমটো মন কৰক: 🍎 🍌 🍇 🍎 🍌 ❓", "दोहराया गया क्रम पहचानें: 🍎 🍌 🍇 🍎 🍌 ❓", "ক্রমটি লক্ষ্য করুন: 🍎 🍌 🍇 🍎 🍌 ❓", "পরিং অমুক হন্না য়েংবীয়ু: 🍎 🍌 🍇 🍎 🍌 ❓", "Peit ia kine ki lai tylli: 🍎 🍌 🍇 🍎 🍌 ❓", "A dawt leha theihai awm tur thlang rawh: 🍎 🍌 🍇 🍎 🍌 ❓", "Line dhyan dibo: 🍎 🍌 🍇 🍎 🍌 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Notice the repeating trio: 🍎 🍌 🍇 🍎 🍌 ❓", "পুনৰাবৃত্ত ক্ৰমটো মন কৰক: 🍎 🍌 🍇 🍎 🍌 ❓", "दोहराया गया क्रम पहचानें: 🍎 🍌 🍇 🍎 🍌 ❓", "ক্রমটি লক্ষ্য করুন: 🍎 🍌 🍇 🍎 🍌 ❓", "পরিং অমুক হন্না য়েংবীয়ু: 🍎 🍌 🍇 🍎 🍌 ❓", "Peit ia kine ki lai tylli: 🍎 🍌 🍇 🍎 🍌 ❓", "A dawt leha theihai awm tur thlang rawh: 🍎 🍌 🍇 🍎 🍌 ❓", "Line dhyan dibo: 🍎 🍌 🍇 🍎 🍌 ❓"),
                    visualEmoji = "🍎 🍌 🍇 🍎 🍌 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🍇 Grapes", "🍇 আঙুৰ", "🍇 अंगूर", "🍇 আঙুর", "🍇 অঙ্গুর", "🍇 Soh drakha", "🍇 Grep", "🍇 Angoor"),
                        getLocalizedText(lang, "🍎 Apple", "🍎 আপেল", "🍎 सेब", "🍎 আপেল", "🍎 সেও", "🍎 Soh saw", "🍎 Apple", "🍎 Apple"),
                        getLocalizedText(lang, "🍉 Watermelon", "🍉 তৰমুজ", "🍉 तरबूज", "🍉 তরমুজ", "🍉 তরমূজ", "🍉 Sohlyngdkhur", "🍉 Dawngei", "🍉 Tormuj"),
                        getLocalizedText(lang, "🍒 Cherry", "🍒 চেৰী", "🍒 चेरी", "🍒 চেরি", "🍒 চেরি", "🍒 Soh cherry", "🍒 Cherry", "🍒 Cherry")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sa_3_2",
                    promptKey = getLocalizedText(lang, "Sequence count: 10, 20, 30, ❓", "দহকৈ গণনা: ১০, ২০, ৩০, ❓", "दस-दस की गिनती: 10, 20, 30, ❓", "দশ করে গণনা: ১০, ২০, ৩০, ❓", "তরা তরা হাপচিনবা: ১০, ২০, ৩০, ❓", "Khein da ki shiphew: 10, 20, 30, ❓", "Sawm zela chhiarin: 10, 20, 30, ❓", "Dosh dosh ginti: 10, 20, 30, ❓"),
                    promptTextFallback = getLocalizedText(lang, "Sequence count: 10, 20, 30, ❓", "দহকৈ গণনা: ১০, ২০, ৩০, ❓", "दस-दस की गिनती: 10, 20, 30, ❓", "দশ করে গণনা: ১০, ২০, ৩০, ❓", "তরা তরা হাপচিনবা: ১০, ২০, ৩০, ❓", "Khein da ki shiphew: 10, 20, 30, ❓", "Sawm zela chhiarin: 10, 20, 30, ❓", "Dosh dosh ginti: 10, 20, 30, ❓"),
                    visualEmoji = "10 ➔ 20 ➔ 30 ➔ ❓",
                    options = listOf("40", "35", "50", "25"),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sa_3_3",
                    promptKey = getLocalizedText(lang, "Arrow directions: ⬆️ ➡️ ⬆️ ➡️ ❓", "কাঁড়ৰ দিশ: ⬆️ ➡️ ⬆️ ➡️ ❓", "तीर की दिशा: ⬆️ ➡️ ⬆️ ➡️ ❓", "তীরের দিক: ⬆️ ➡️ ⬆️ ➡️ ❓", "মায়কৈ: ⬆️ ➡️ ⬆️ ➡️ ❓", "Phai sha kano: ⬆️ ➡️ ⬆️ ➡️ ❓", "Arrow kawhna dawt tur: ⬆️ ➡️ ⬆️ ➡️ ❓", "Teer laga direction: ⬆️ ➡️ ⬆️ ➡️ ❓"),
                    promptTextFallback = getLocalizedText(lang, "Arrow directions: ⬆️ ➡️ ⬆️ ➡️ ❓", "কাঁড়ৰ দিশ: ⬆️ ➡️ ⬆️ ➡️ ❓", "तीर की दिशा: ⬆️ ➡️ ⬆️ ➡️ ❓", "তীরের দিক: ⬆️ ➡️ ⬆️ ➡️ ❓", "মায়কৈ: ⬆️ ➡️ ⬆️ ➡️ ❓", "Phai sha kano: ⬆️ ➡️ ⬆️ ➡️ ❓", "Arrow kawhna dawt tur: ⬆️ ➡️ ⬆️ ➡️ ❓", "Teer laga direction: ⬆️ ➡️ ⬆️ ➡️ ❓"),
                    visualEmoji = "⬆️ ➡️ ⬆️ ➡️ ❓",
                    options = listOf(
                        getLocalizedText(lang, "⬆️ Up", "⬆️ ওপৰলৈ", "⬆️ ऊपर", "⬆️ উপরে", "⬆️ ওন্থোকপা", "⬆️ Shaneng", "⬆️ Chunglam", "⬆️ Uporte"),
                        getLocalizedText(lang, "⬇️ Down", "⬇️ তললৈ", "⬇️ नीचे", "⬇️ নিচে", "⬇️ মখাদা", "⬇️ Shapoh", "⬇️ Hnuailam", "⬇️ Tolte"),
                        getLocalizedText(lang, "⬅️ Left", "⬅️ বাওঁফালে", "⬅️ बाएँ", "⬅️ বামে", "⬅️ ওইরোমদা", "⬅️ Sha kadiang", "⬅️ Veilam", "⬅️ Baamte"),
                        getLocalizedText(lang, "🔄 Turn", "🔄 ঘূৰ্ণন", "🔄 घूमना", "🔄 ঘোরা", "🔄 কোয়শিনবা", "🔄 Sawdong", "🔄 Her kual", "🔄 Ghuma")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generateSimplePatternQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "sp_1_1",
                    promptKey = getLocalizedText(lang, "Big and Small pattern: 🐘 🐁 🐘 ❓", "ডাঙৰ আৰু সৰুৰ আৰ্হি: 🐘 🐁 🐘 ❓", "बड़ा और छोटा क्रम: 🐘 🐁 🐘 ❓", "বড় এবং ছোট প্যাটার্ন: 🐘 🐁 🐘 ❓", "চাউবা অমসুং পিকপা পেটার্ন: 🐘 🐁 🐘 ❓", "Heh bad Rit: 🐘 🐁 🐘 ❓", "Lian leh Te: 🐘 🐁 🐘 ❓", "Dangor aru Chutu: 🐘 🐁 🐘 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Big and Small pattern: 🐘 🐁 🐘 ❓", "ডাঙৰ আৰু সৰুৰ আৰ্হি: 🐘 🐁 🐘 ❓", "बड़ा और छोटा क्रम: 🐘 🐁 🐘 ❓", "বড় এবং ছোট প্যাটার্ন: 🐘 🐁 🐘 ❓", "চাউবা অমসুং পিকপা পেটার্ন: 🐘 🐁 🐘 ❓", "Heh bad Rit: 🐘 🐁 🐘 ❓", "Lian leh Te: 🐘 🐁 🐘 ❓", "Dangor aru Chutu: 🐘 🐁 🐘 ❓"),
                    visualEmoji = "🐘 🐁 🐘 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🐁 Small Mouse", "🐁 সৰু নিগনি", "🐁 छोटा चूहा", "🐁 ছোট ইঁদুর", "🐁 পিকপা য়ুচি", "🐁 Khnai rit", "🐁 Sa-zu te", "🐁 Chutu Neko"),
                        getLocalizedText(lang, "🐘 Big Elephant", "🐘 ডাঙৰ হাতী", "🐘 बड़ा हाथी", "🐘 বড় হাতি", "🐘 চাউবা শমু", "🐘 Hati heh", "🐘 Sai lian", "🐘 Dangor Haathi")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sp_1_2",
                    promptKey = getLocalizedText(lang, "Day and Night rhythm: ☀️ 🌙 ☀️ ❓", "দিন আৰু ৰাতিৰ ছন্দ: ☀️ 🌙 ☀️ ❓", "दिन और रात का चक्र: ☀️ 🌙 ☀️ ❓", "দিন এবং রাতের ছন্দ: ☀️ 🌙 ☀️ ❓", "নুমিৎ অমসুং অহিং: ☀️ 🌙 ☀️ ❓", "Sngi bad Miet: ☀️ 🌙 ☀️ ❓", "Chhun leh Zan: ☀️ 🌙 ☀️ ❓", "Din aru Raat: ☀️ 🌙 ☀️ ❓"),
                    promptTextFallback = getLocalizedText(lang, "Day and Night rhythm: ☀️ 🌙 ☀️ ❓", "দিন আৰু ৰাতিৰ ছন্দ: ☀️ 🌙 ☀️ ❓", "दिन और रात का चक्र: ☀️ 🌙 ☀️ ❓", "দিন এবং রাতের ছন্দ: ☀️ 🌙 ☀️ ❓", "নুমিৎ অমসুং অহিং: ☀️ 🌙 ☀️ ❓", "Sngi bad Miet: ☀️ 🌙 ☀️ ❓", "Chhun leh Zan: ☀️ 🌙 ☀️ ❓", "Din aru Raat: ☀️ 🌙 ☀️ ❓"),
                    visualEmoji = "☀️ 🌙 ☀️ ❓",
                    options = listOf(
                        getLocalizedText(lang, "🌙 Crescent Moon", "🌙 জোনাকী জোনবাই", "🌙 चाँद", "🌙 চাঁদ", "🌙 থা", "🌙 U bnai", "🌙 Thla", "🌙 Chand"),
                        getLocalizedText(lang, "☀️ Morning Sun", "☀️ পুৱাৰ বেলি", "☀️ सुबह का सूरज", "☀️ সকালের সূর্য", "☀️ নুমিৎ", "☀️ Ka sngi", "☀️ Ni", "☀️ Suraj")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "sp_2_1",
                    promptKey = getLocalizedText(lang, "Plant growth pattern: 🌱 🌿 🌳 🌱 🌿 ❓", "উদ্ভিদৰ বৃদ্ধিৰ আৰ্হি: 🌱 🌿 🌳 🌱 🌿 ❓", "पौधे का विकास क्रम: 🌱 🌿 🌳 🌱 🌿 ❓", "গাছের বৃদ্ধির প্যাটার্ন: 🌱 🌿 🌳 🌱 🌿 ❓", "পাম্বী চাউখৎপগী পেটার্ন: 🌱 🌿 🌳 🌱 🌿 ❓", "Jingsan ki jingthung: 🌱 🌿 🌳 🌱 🌿 ❓", "Thing thang chho: 🌱 🌿 🌳 🌱 🌿 ❓", "Gach dangor hua: 🌱 🌿 🌳 🌱 🌿 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Plant growth pattern: 🌱 🌿 🌳 🌱 🌿 ❓", "উদ্ভিদৰ বৃদ্ধিৰ আৰ্হি: 🌱 🌿 🌳 🌱 🌿 ❓", "पौधे का विकास क्रम: 🌱 🌿 🌳 🌱 🌿 ❓", "গাছের বৃদ্ধির প্যাটার্ন: 🌱 🌿 🌳 🌱 🌿 ❓", "পাম্বী চাউখৎপগী পেটার্ন: 🌱 🌿 🌳 🌱 🌿 ❓", "Jingsan ki jingthung: 🌱 🌿 🌳 🌱 🌿 ❓", "Thing thang chho: 🌱 🌿 🌳 🌱 🌿 ❓", "Gach dangor hua: 🌱 🌿 🌳 🌱 🌿 ❓"),
                    visualEmoji = "🌱 🌿 🌳 🌱 🌿 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🌳 Big Tree", "🌳 ডাঙৰ গছ", "🌳 बड़ा पेड़", "🌳 বড় গাছ", "🌳 চাউবা উ", "🌳 Dieng heh", "🌳 Thingpui lian", "🌳 Dangor Gach"),
                        getLocalizedText(lang, "🌱 Sprout", "🌱 পুলি", "🌱 अंकुर", "🌱 চারা", "🌱 হৌলক্লিবা", "🌱 Lung", "🌱 Tiak", "🌱 Chutu Gach"),
                        getLocalizedText(lang, "🍂 Dry Leaf", "🍂 শুকান পাত", "🍂 सूखा पत्ता", "🍂 শুকনো পাতা", "🍂 কংশিবা মনা", "🍂 Sla tyrkhong", "🍂 Hnah ro", "🍂 Sukha Paata")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sp_2_2",
                    promptKey = getLocalizedText(lang, "Color rhythm: 🟩 🟨 🟩 🟨 ❓", "ৰঙৰ ছন্দ: 🟩 🟨 🟩 🟨 ❓", "रंगों का क्रम: 🟩 🟨 🟩 🟨 ❓", "রঙের ছন্দ: 🟩 🟨 🟩 🟨 ❓", "মচু পরিং: 🟩 🟨 🟩 🟨 ❓", "Rong ba bud: 🟩 🟨 🟩 🟨 ❓", "Rawng inthlak kual: 🟩 🟨 🟩 🟨 ❓", "Rong milaibo: 🟩 🟨 🟩 🟨 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Color rhythm: 🟩 🟨 🟩 🟨 ❓", "ৰঙৰ ছন্দ: 🟩 🟨 🟩 🟨 ❓", "रंगों का क्रम: 🟩 🟨 🟩 🟨 ❓", "রঙের ছন্দ: 🟩 🟨 🟩 🟨 ❓", "মচু পরিং: 🟩 🟨 🟩 🟨 ❓", "Rong ba bud: 🟩 🟨 🟩 🟨 ❓", "Rawng inthlak kual: 🟩 🟨 🟩 🟨 ❓", "Rong milaibo: 🟩 🟨 🟩 🟨 ❓"),
                    visualEmoji = "🟩 🟨 🟩 🟨 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🟩 Green", "🟩 সেউজীয়া", "🟩 हरा", "🟩 সবুজ", "🟩 অশংবা", "🟩 Jyrngam", "🟩 Hring", "🟩 Horia"),
                        getLocalizedText(lang, "🟥 Red", "🟥 ৰঙা", "🟥 लाल", "🟥 লাল", "🟥 অঙাংবা", "🟥 Saw", "🟥 Sen", "🟥 Lal"),
                        getLocalizedText(lang, "🟦 Blue", "🟦 নীলা", "🟦 नीला", "🟦 নীল", "🟦 হিগোক", "🟦 Jylliew", "🟦 Pawl", "🟦 Neela")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sp_2_3",
                    promptKey = getLocalizedText(lang, "Sound rhythm: 🔔 🔕 🔔 🔕 ❓", "শব্দৰ ছন্দ: 🔔 🔕 🔔 🔕 ❓", "ध्वनि का क्रम: 🔔 🔕 🔔 🔕 ❓", "শব্দের ছন্দ: 🔔 🔕 🔔 🔕 ❓", "খোনজেল পরিং: 🔔 🔕 🔔 🔕 ❓", "Sur ba bud: 🔔 🔕 🔔 🔕 ❓", "Ri inthlak: 🔔 🔕 🔔 🔕 ❓", "Awaj milaibo: 🔔 🔕 🔔 🔕 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Sound rhythm: 🔔 🔕 🔔 🔕 ❓", "শব্দৰ ছন্দ: 🔔 🔕 🔔 🔕 ❓", "ध्वनि का क्रम: 🔔 🔕 🔔 🔕 ❓", "শব্দের ছন্দ: 🔔 🔕 🔔 🔕 ❓", "খোনজেল পরিং: 🔔 🔕 🔔 🔕 ❓", "Sur ba bud: 🔔 🔕 🔔 🔕 ❓", "Ri inthlak: 🔔 🔕 🔔 🔕 ❓", "Awaj milaibo: 🔔 🔕 🔔 🔕 ❓"),
                    visualEmoji = "🔔 🔕 🔔 🔕 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🔔 Ringing Bell", "🔔 বাজি থকা ঘণ্টা", "🔔 बजती घंटी", "🔔 বাজা ঘণ্টা", "🔔 তানবা ঘন্তা", "🔔 Shakuria sawa", "🔔 Dar ri", "🔔 Ghanti awaj"),
                        getLocalizedText(lang, "🔕 Mute", "🔕 নীৰৱ", "🔕 शांत", "🔕 শব্দহীন", "🔕 নিমথাং", "🔕 Sngap jar", "🔕 Thawm reh", "🔕 Chup chap"),
                        getLocalizedText(lang, "📢 Loudspeaker", "📢 মাইক", "📢 लाउडस्पीकर", "📢 মাইক", "📢 মাইক", "📢 Loudspeaker", "📢 Au rinna", "📢 Mike")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "sp_3_1",
                    promptKey = getLocalizedText(lang, "Which block fits the symmetry? 🟥 🟦 🟩 🟥 🟦 ❓", "কোনটো ৰঙে এই ক্ৰম সম্পূৰ্ণ কৰিব? 🟥 🟦 🟩 🟥 🟦 ❓", "कौन सा ब्लॉक इस सममिति में फिट बैठता है? 🟥 🟦 🟩 🟥 🟦 ❓", "কোনটি এই ক্রম সম্পূর্ণ করবে? 🟥 🟦 🟩 🟥 🟦 ❓", "করম্বা মচুনা পেটার্ন লোইশিনগদগে? 🟥 🟦 🟩 🟥 🟦 ❓", "Kano ka block ba iahap? 🟥 🟦 🟩 🟥 🟦 ❓", "A dawtah eng rawng nge awm ang? 🟥 🟦 🟩 🟥 🟦 ❓", "Kun rong ahibo? 🟥 🟦 🟩 🟥 🟦 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Which block fits the symmetry? 🟥 🟦 🟩 🟥 🟦 ❓", "কোনটো ৰঙে এই ক্ৰম সম্পূৰ্ণ কৰিব? 🟥 🟦 🟩 🟥 🟦 ❓", "कौन सा ब्लॉक इस सममिति में फिट बैठता है? 🟥 🟦 🟩 🟥 🟦 ❓", "কোনটি এই ক্রম সম্পূর্ণ করবে? 🟥 🟦 🟩 🟥 🟦 ❓", "করম্বা মচুনা পেটার্ন লোইশিনগদগে? 🟥 🟦 🟩 🟥 🟦 ❓", "Kano ka block ba iahap? 🟥 🟦 🟩 🟥 🟦 ❓", "A dawtah eng rawng nge awm ang? 🟥 🟦 🟩 🟥 🟦 ❓", "Kun rong ahibo? 🟥 🟦 🟩 🟥 🟦 ❓"),
                    visualEmoji = "🟥 🟦 🟩 🟥 🟦 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🟩 Green Block", "🟩 সেউজীয়া", "🟩 हरा ब्लॉक", "🟩 সবুজ ব্লক", "🟩 অশংবা", "🟩 Block jyrngam", "🟩 Hring", "🟩 Horia"),
                        getLocalizedText(lang, "🟨 Yellow Block", "🟨 হালধীয়া", "🟨 पीला ब्लॉक", "🟨 হলুদ ব্লক", "🟨 ঙাংবা", "🟨 Block stem", "🟨 Eng", "🟨 Haldi"),
                        getLocalizedText(lang, "⬛ Black Block", "⬛ ক'লা", "⬛ काला ब्लॉक", "⬛ কালো ব্লক", "⬛ অমৌবা", "⬛ Block iong", "⬛ Dum", "⬛ Kala"),
                        getLocalizedText(lang, "🟥 Red Block", "🟥 ৰঙা", "🟥 लाल ब्लॉक", "🟥 লাল ব্লক", "🟥 অঙাংবা", "🟥 Block saw", "🟥 Sen", "🟥 Lal")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sp_3_2",
                    promptKey = getLocalizedText(lang, "Moon phases: 🌑 🌓 🌕 🌑 🌓 ❓", "জোনৰ ক্ৰম: 🌑 🌓 🌕 🌑 🌓 ❓", "चंद्रमा की कलाएं: 🌑 🌓 🌕 🌑 🌓 ❓", "চাঁদের কলা: 🌑 🌓 🌕 🌑 🌓 ❓", "থাগী মশক: 🌑 🌓 🌕 🌑 🌓 ❓", "Ka rukom jong u bnai: 🌑 🌓 🌕 🌑 🌓 ❓", "Thla awm dan: 🌑 🌓 🌕 🌑 🌓 ❓", "Chand laga line: 🌑 🌓 🌕 🌑 🌓 ❓"),
                    promptTextFallback = getLocalizedText(lang, "Moon phases: 🌑 🌓 🌕 🌑 🌓 ❓", "জোনৰ ক্ৰম: 🌑 🌓 🌕 🌑 🌓 ❓", "चंद्रमा की कलाएं: 🌑 🌓 🌕 🌑 🌓 ❓", "চাঁদের কলা: 🌑 🌓 🌕 🌑 🌓 ❓", "থাগী মশক: 🌑 🌓 🌕 🌑 🌓 ❓", "Ka rukom jong u bnai: 🌑 🌓 🌕 🌑 🌓 ❓", "Thla awm dan: 🌑 🌓 🌕 🌑 🌓 ❓", "Chand laga line: 🌑 🌓 🌕 🌑 🌓 ❓"),
                    visualEmoji = "🌑 🌓 🌕 🌑 🌓 ❓",
                    options = listOf(
                        getLocalizedText(lang, "🌕 Full Moon", "🌕 পূৰ্ণিমাৰ জোন", "🌕 पूर्णिमा (पूरा चाँद)", "🌕 পূর্ণিমার চাঁদ", "🌕 থাবা থা", "🌕 U bnai pura", "🌕 Thla eng pum", "🌕 Pura Chand"),
                        getLocalizedText(lang, "🌑 New Moon", "🌑 অমাৱস্যা", "🌑 अमावस्या", "🌑 অমাবস্যা", "🌑 অমাৱস্যা", "🌑 U bnai dum", "🌑 Thla thim", "🌑 Amavasya"),
                        getLocalizedText(lang, "⭐ Bright Star", "⭐ উজ্বল তৰা", "⭐ चमकता तारा", "⭐ উজ্জ্বল তারা", "⭐ থৱানমিচাক", "⭐ Khlur", "⭐ Arsi", "⭐ Tara"),
                        getLocalizedText(lang, "☀️ Sun", "☀️ সূৰ্য্য", "☀️ सूरज", "☀️ সূর্য", "☀️ নুমিৎ", "☀️ Sngi", "☀️ Ni", "☀️ Suraj")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "sp_3_3",
                    promptKey = getLocalizedText(lang, "Tea garden rhythm: 🍃 ☕ 🍃 ☕ ❓", "চাহ বাগিচাৰ ছন্দ: 🍃 ☕ 🍃 ☕ ❓", "चाय बागान का चक्र: 🍃 ☕ 🍃 ☕ ❓", "চা বাগানের ছন্দ: 🍃 ☕ 🍃 ☕ ❓", "চা পাম্বী পরিং: 🍃 ☕ 🍃 ☕ ❓", "Kper sha: 🍃 ☕ 🍃 ☕ ❓", "Thingpui huan: 🍃 ☕ 🍃 ☕ ❓", "Cha Bagan line: 🍃 ☕ 🍃 ☕ ❓"),
                    promptTextFallback = getLocalizedText(lang, "Tea garden rhythm: 🍃 ☕ 🍃 ☕ ❓", "চাহ বাগিচাৰ ছন্দ: 🍃 ☕ 🍃 ☕ ❓", "चाय बागान का चक्र: 🍃 ☕ 🍃 ☕ ❓", "চা বাগানের ছন্দ: 🍃 ☕ 🍃 ☕ ❓", "চা পাম্বী পরিং: 🍃 ☕ 🍃 ☕ ❓", "Kper sha: 🍃 ☕ 🍃 ☕ ❓", "Thingpui huan: 🍃 ☕ 🍃 ☕ ❓", "Cha Bagan line: 🍃 ☕ 🍃 ☕ ❓"),
                    visualEmoji = "🍃 ☕ 🍃 ☕ ❓",
                    options = listOf(
                        getLocalizedText(lang, "🍃 Fresh Leaf", "🍃 সতেজ পাত", "🍃 ताज़ा पत्ता", "🍃 সতেজ পাতা", "🍃 অশংবা মনা", "🍃 Sla khuid", "🍃 Hnah hring", "🍃 Taza Paata"),
                        getLocalizedText(lang, "🫖 Big Kettle", "🫖 ডাঙৰ কেটলী", "🫖 बड़ी केतली", "🫖 কেটলি", "🫖 কেতলী", "🫖 Khiew heh", "🫖 Bel lian", "🫖 Kettle"),
                        getLocalizedText(lang, "🍯 Honey Jar", "🍯 মৌৰ বটল", "🍯 शहद", "🍯 মধুর পাত্র", "🍯 খোইহী", "🍯 Khiew ngap", "🍯 Khawivah tui", "🍯 Madhu"),
                        getLocalizedText(lang, "🥖 Bread", "🥖 পাউৰুটী", "🥖 रोटी", "🥖 পাউরুটি", "🥖 তান", "🥖 Ruti", "🥖 Chhang", "🥖 Roti")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    private fun generateEverydayChoiceQuestions(level: Int, lang: String): List<GameQuestion> {
        return when (level) {
            1 -> listOf(
                GameQuestion(
                    id = "ec_1_1",
                    promptKey = getLocalizedText(lang, "It is raining outside before stepping out. What should you take?", "বাহিৰত বৰষুণ দি আছে। ওলাই যাওঁতে কি লগত ল'ব?", "बाहर बारिश हो रही है। बाहर जाते समय आपको क्या लेना चाहिए?", "বাইরে বৃষ্টি হচ্ছে। বেরোনোর আগে সাথে কী নেওয়া উচিত?", "নোং চুরবদি মপান থোকপদা করি লৌশিনগদগে?", "U slap u la thep shabar. Aiu phi dei ban shim?", "Pawnah ruah a sur a, chhuah dawnah eng nge i ken ang?", "Bahar te boroxun asey. Ki loi kene jabo?"),
                    promptTextFallback = getLocalizedText(lang, "It is raining outside before stepping out. What should you take?", "বাহিৰত বৰষুণ দি আছে। ওলাই যাওঁতে কি লগত ল'ব?", "बाहर बारिश हो रही है। बाहर जाते समय आपको क्या लेना चाहिए?", "বাইরে বৃষ্টি হচ্ছে। বেরোনোর আগে সাথে কী নেওয়া উচিত?", "নোং চুরবদি মপান থোকপদা করি লৌশিনগদগে?", "U slap u la thep shabar. Aiu phi dei ban shim?", "Pawnah ruah a sur a, chhuah dawnah eng nge i ken ang?", "Bahar te boroxun asey. Ki loi kene jabo?"),
                    visualEmoji = "🌧️",
                    options = listOf(
                        getLocalizedText(lang, "☔ Umbrella", "☔ ছাতি", "☔ छाता", "☔ ছাতা", "☔ শমব্রাক", "☔ Chatri", "☔ Nihliap", "☔ Chati"),
                        getLocalizedText(lang, "🕶️ Sunglasses", "🕶️ ৰ'দৰ চশমা", "🕶️ धूप का चश्मा", "🕶️ সানগ্লাস", "🕶️ চশমা", "🕶️ Itshmat sngi", "🕶️ Ni lakah tarmit", "🕶️ Kala Chasma")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ec_1_2",
                    promptKey = getLocalizedText(lang, "Before sitting down for lunch, what is the best habit?", "দুপৰীয়াৰ আহাৰ খোৱাৰ আগতে কোনটো ভাল অভ্যাস?", "दोपहर के भोजन से पहले सबसे अच्छी आदत क्या है?", "দুপুরের খাবারের আগে সবচেয়ে ভালো অভ্যাস কোনটি?", "চাক চানবা ফমদ্রিঙৈদা খ্বাইদগী ফবা চৎনবী করিনো?", "Shwa ban bam ja, aiu kaba bha tam ban leh?", "Chaw ei hmain eng tih nge tha ber?", "Khana khabole aage ki kora bhal asey?"),
                    promptTextFallback = getLocalizedText(lang, "Before sitting down for lunch, what is the best habit?", "দুপৰীয়াৰ আহাৰ খোৱাৰ আগতে কোনটো ভাল অভ্যাস?", "दोपहर के भोजन से पहले सबसे अच्छी आदत क्या है?", "দুপুরের খাবারের আগে সবচেয়ে ভালো অভ্যাস কোনটি?", "চাক চানবা ফমদ্রিঙৈদা খ্বাইদগী ফবা চৎনবী করিনো?", "Shwa ban bam ja, aiu kaba bha tam ban leh?", "Chaw ei hmain eng tih nge tha ber?", "Khana khabole aage ki kora bhal asey?"),
                    visualEmoji = "🧼",
                    options = listOf(
                        getLocalizedText(lang, "Wash hands with soap 🧼", "চাবোনেৰে হাত ধোৱা 🧼", "साबुन से हाथ धोना 🧼", "সাবান দিয়ে হাত ধোয়া 🧼", "খুৎ শাবুন্না লোইবা 🧼", "Sait kti da sabon 🧼", "Sabona kut silh 🧼", "Sabun para haath dhuwa 🧼"),
                        getLocalizedText(lang, "Put on walking shoes 👟", "খোজ কঢ়া জোতা পিন্ধা 👟", "जूते पहनना 👟", "জুতো পরা 👟", "খুৎশম শেৎপা 👟", "Kup juti 👟", "Pheikhawk bun 👟", "Juta lagabo 👟")
                    ),
                    correctIndex = 0
                )
            )
            2 -> listOf(
                GameQuestion(
                    id = "ec_2_1",
                    promptKey = getLocalizedText(lang, "You feel thirsty in the warm afternoon. What should you drink?", "দুপৰীয়া পিয়াহ লাগিলে কি খাব লাগে?", "दोपहर में प्यास लगने पर क्या पीना चाहिए?", "দুপুরে তৃষ্ণা পেলে কী পান করা উচিত?", "দুপোরদা ঈশিং তেক্তরবা মতমদা করি থকপগে?", "Lada phi sliang um ha ka por sngi, aiu phi dei ban dih?", "Chhun khawlum laia i tuihalin eng nge i in ang?", "Pyas lagile ki khabo lage?"),
                    promptTextFallback = getLocalizedText(lang, "You feel thirsty in the warm afternoon. What should you drink?", "দুপৰীয়া পিয়াহ লাগিলে কি খাব লাগে?", "दोपहर में प्यास लगने पर क्या पीना चाहिए?", "দুপুরে তৃষ্ণা পেলে কী পান করা উচিত?", "দুপোরদা ঈশিং তেক্তরবা মতমদা করি থকপগে?", "Lada phi sliang um ha ka por sngi, aiu phi dei ban dih?", "Chhun khawlum laia i tuihalin eng nge i in ang?", "Pyas lagile ki khabo lage?"),
                    visualEmoji = "💧",
                    options = listOf(
                        getLocalizedText(lang, "Fresh Clean Water 💧", "পৰিষ্কাৰ খোৱাপানী 💧", "ताज़ा साफ पानी 💧", "পরিষ্কার বিশুদ্ধ জল 💧", "শেংলবা ঈশিং 💧", "Um khuid 💧", "Tui thianghlim 💧", "Saaf Paani 💧"),
                        getLocalizedText(lang, "Cooking Oil 🛢️", "খোৱা তেল 🛢️", "खाना पकाने का तेल 🛢️", "রান্নার তেল 🛢️", "থৌ 🛢️", "Umphniang shetja 🛢️", "Chawhmeh chhum tel 🛢️", "Tel 🛢️"),
                        getLocalizedText(lang, "Liquid Detergent 🧴", "ডিটাৰজেণ্ট 🧴", "सफाई का सर्फ 🧴", "ডিটারজেন্ট 🧴", "সাবোন ঈশিং 🧴", "Dawaw sait jain 🧴", "Sukna sabon tui 🧴", "Sabun Paani 🧴")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ec_2_2",
                    promptKey = getLocalizedText(lang, "The sun has set and it is dark in the room. What is the safest choice?", "সূৰ্য্য ডুবিল আৰু কোঠাটোত আন্ধাৰ হ'ল। সুৰক্ষিত বিকল্প কি?", "कमरे में अंधेरा होने पर सबसे सुरक्षित विकल्प क्या है?", "ঘরে অন্ধকার হলে সবচেয়ে নিরাপদ উপায় কী?", "নুমিৎ তাখ্রে অমসুং কা মনুং আম্বা ওইরে। করিনো খ্বাইদগী চুনবা?", "Lada dum ha kamra, aiu kaba shngain tam ban leh?", "Pindan chhung a thim chuan him ber tura tih tur eng nge?", "Room te andhera hole sobse bhal ki asey?"),
                    promptTextFallback = getLocalizedText(lang, "The sun has set and it is dark in the room. What is the safest choice?", "সূৰ্য্য ডুবিল আৰু কোঠাটোত আন্ধাৰ হ'ল। সুৰক্ষিত বিকল্প কি?", "कमरे में अंधेरा होने पर सबसे सुरक्षित विकल्प क्या है?", "ঘরে অন্ধকার হলে সবচেয়ে নিরাপদ উপায় কী?", "নুমিৎ তাখ্রে অমসুং কা মনুং আম্বা ওইরে। করিনো খ্বাইদগী চুনবা?", "Lada dum ha kamra, aiu kaba shngain tam ban leh?", "Pindan chhung a thim chuan him ber tura tih tur eng nge?", "Room te andhera hole sobse bhal ki asey?"),
                    visualEmoji = "💡",
                    options = listOf(
                        getLocalizedText(lang, "Turn on the room light 💡", "কোঠাৰ লাইট জ্বলাই দিয়া 💡", "कमरे की बत्ती जलाना 💡", "ঘরের আলো জ্বালানো 💡", "কা মনুংগী মৈ থানবা 💡", "Pynmeh ding 💡", "Khawvel ti eng rawh 💡", "Batti jolai dibo 💡"),
                        getLocalizedText(lang, "Walk blindly in dark 🚶", "আন্ধাৰতে খোজ কঢ়া 🚶", "अंधेरे में चलना 🚶", "অন্ধকারে হাঁটা 🚶", "অমম্বদা চৎপা 🚶", "Ia-aid ha ka jingdum 🚶", "Thim hnuaiah kal mai 🚶", "Andhera te berabo 🚶"),
                        getLocalizedText(lang, "Close all doors tight 🚪", "সকলো দুৱাৰ বন্ধ কৰা 🚪", "दरवाजे बंद करना 🚪", "সব দরজা বন্ধ করা 🚪", "থোং লোন্সিবা 🚪", "Khang baroh 🚪", "Kawngkhar khar tlat 🚪", "Duar bondha kora 🚪")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ec_2_3",
                    promptKey = getLocalizedText(lang, "In chilly winter weather, what should you wear?", "শীতকালৰ ঠাণ্ডাত কি পৰিধান কৰা উচিত?", "सर्दियों के ठंडे मौसम में क्या पहनना चाहिए?", "শীতের ঠান্ডায় কী পরা উচিত?", "ইংবা মতমদা করি শেৎপা চুনবগে?", "Ha ka por tlang, aiu phi dei ban kup?", "Thlasik vawh laiin eng kawr nge hak tur?", "Thanda din te ki kapor lagabo?"),
                    promptTextFallback = getLocalizedText(lang, "In chilly winter weather, what should you wear?", "শীতকালৰ ঠাণ্ডাত কি পৰিধান কৰা উচিত?", "सर्दियों के ठंडे मौसम में क्या पहनना चाहिए?", "শীতের ঠান্ডায় কী পরা উচিত?", "ইংবা মতমদা করি শেৎপা চুনবগে?", "Ha ka por tlang, aiu phi dei ban kup?", "Thlasik vawh laiin eng kawr nge hak tur?", "Thanda din te ki kapor lagabo?"),
                    visualEmoji = "🧥",
                    options = listOf(
                        getLocalizedText(lang, "Warm Woolen Sweater / Shawl 🧥", "উমাল চুৱেটাৰ বা শাল 🧥", "गर्म ऊनी स्वेटर या शॉल 🧥", "উষ্ণ সোয়েটার বা শাল 🧥", "শাফবা ফি শেৎপা 🧥", "Sweater syaid 🧥", "Sweater lum 🧥", "Garm Sweater 🧥"),
                        getLocalizedText(lang, "Thin Cotton Vest 🎽", "পাতল কপাহী গেঞ্জী 🎽", "पतला बनियान 🎽", "পাতলা সুতি গেঞ্জি 🎽", "তেনবা ফি 🎽", "Banyan stang 🎽", "Banyan pan 🎽", "Patla Banyan 🎽"),
                        getLocalizedText(lang, "Swimming Trunks 🩲", "সাঁতোৰা কাপোৰ 🩲", "तैराकी कपड़ा 🩲", "সাঁতারের ট্রাঙ্ক 🩲", "ইরুজবা ফি 🩲", "Jain jngi 🩲", "Inbual kekawrte 🩲", "Swim Trunks 🩲")
                    ),
                    correctIndex = 0
                )
            )
            else -> listOf(
                GameQuestion(
                    id = "ec_3_1",
                    promptKey = getLocalizedText(lang, "You hear a knock at your door. What is the safest first step?", "দুৱাৰত টোকৰ শুনা পালে প্ৰথমে কি কৰা উচিত?", "दरवाजे पर खटखटाहट सुनने पर सबसे पहले क्या करना सुरक्षित है?", "দরজায় কড়া নাড়ার শব্দ শুনলে প্রথমে কী করা নিরাপদ?", "থোংদা খোল্লাকপা তাবদা অহানবা খোংথাং করিনো?", "Lada don ba thab ha jingkhang, aiu ban leh nyngkong?", "Kawngkhar kik thawm i hriat chuan eng nge tih hmasak ber tur?", "Duar te awaj hole ki koribo?"),
                    promptTextFallback = getLocalizedText(lang, "You hear a knock at your door. What is the safest first step?", "দুৱাৰত টোকৰ শুনা পালে প্ৰথমে কি কৰা উচিত?", "दरवाजे पर खटखटाहट सुनने पर सबसे पहले क्या करना सुरक्षित है?", "দরজায় কড়া নাড়ার শব্দ শুনলে প্রথমে কী করা নিরাপদ?", "থোংদা খোল্লাকপা তাবদা অহানবা খোংথাং করিনো?", "Lada don ba thab ha jingkhang, aiu ban leh nyngkong?", "Kawngkhar kik thawm i hriat chuan eng nge tih hmasak ber tur?", "Duar te awaj hole ki koribo?"),
                    visualEmoji = "🚪",
                    options = listOf(
                        getLocalizedText(lang, "Ask who it is before opening 🚪", "খোলাৰ আগতে কোন সুধি লওক 🚪", "खोलने से पहले पूछें कि कौन है 🚪", "খোলার আগে জিজ্ঞাসা করুন কে 🚪", "থোং হাংদ্রিঙৈদা কনানো হংবা 🚪", "Kylli mano ba don shabar 🚪", "Tunge a nih zawt hmasa rawh 🚪", "Khulibo aage puchhibo kun asey 🚪"),
                        getLocalizedText(lang, "Immediately run outside 🏃", "লৰালৰিকৈ ওলাই যোৱা 🏃", "तुरंत बाहर भाग जाना 🏃", "দৌড়ে বাইরে যাওয়া 🏃", "মপানদা চেনবা 🏃", "Phet shabar 🏃", "Tlan chhuak nghal mai 🏃", "Bahar dhoribo 🏃"),
                        getLocalizedText(lang, "Hide quietly under bed 🛌", "বিচনাৰ তলত লুকোৱা 🛌", "बिस्तर के नीचे छिपना 🛌", "খাটের নিচে লুকানো 🛌", "তুম্ফম মখাদা লোৎপা 🛌", "Rieh hapoh jingthiah 🛌", "Khum hnuaiah biru 🛌", "Palong niche lukabo 🛌"),
                        getLocalizedText(lang, "Throw water at door 🪣", "দুৱাৰত পানী ঢালি দিয়া 🪣", "दरवाजे पर पानी फेंकना 🪣", "দরজায় জল ঢালা 🪣", "ঈশিং চংবা 🪣", "Theh um 🪣", "Tui theh rawh 🪣", "Paani phikibo 🪣")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ec_3_2",
                    promptKey = getLocalizedText(lang, "Your doctor prescribed a medicine with water. When should you take it?", "চিকিৎসকে দিয়া ঔষধ কেতিয়া খোৱা উচিত?", "डॉक्टर द्वारा दी गई दवा कब लेनी चाहिए?", "ডাক্তারের দেওয়া ওষুধ কখন খাওয়া উচিত?", "দোক্তরনা পীরম্বা হিদাক করম্বা মতমদা চাবগে?", "Mano ban dih ia ka dawai?", "Doctor damdawi chawh engtikah nge i ei ang?", "Doctor laga dowa kun time te khabo?"),
                    promptTextFallback = getLocalizedText(lang, "Your doctor prescribed a medicine with water. When should you take it?", "চিকিৎসকে দিয়া ঔষধ কেতিয়া খোৱা উচিত?", "डॉक्टर द्वारा दी गई दवा कब लेनी चाहिए?", "ডাক্তারের দেওয়া ওষুধ কখন খাওয়া উচিত?", "দোক্তরনা পীরম্বা হিদাক করম্বা মতমদা চাবগে?", "Mano ban dih ia ka dawai?", "Doctor damdawi chawh engtikah nge i ei ang?", "Doctor laga dowa kun time te khabo?"),
                    visualEmoji = "💊",
                    options = listOf(
                        getLocalizedText(lang, "At the scheduled time with water 💊", "নিৰ্ধাৰিত সময়ত পানীৰ সৈতে 💊", "निर्धारित समय पर पानी के साथ 💊", "নির্দিষ্ট সময়ে জলের সাথে 💊", "লেপলবা মতমদা ঈশিংগা চাবা 💊", "Ha ka por ba la buh 💊", "Hun ruahman ang thlapin 💊", "Time te paani logote 💊"),
                        getLocalizedText(lang, "Double the dose at night 🌙", "ৰাতি দুগুণ খোৱা 🌙", "रात में दोगुनी खुराक लेना 🌙", "রাতে দ্বিগুণ খাওয়া 🌙", "অহিংদা শরুক অনী চাবা 🌙", "Dih ar sien 🌙", "A let hnih ei 🌙", "Rati double khabo 🌙"),
                        getLocalizedText(lang, "Never take medicine ❌", "কেতিয়াও ঔষধ নোখোৱা ❌", "दवा कभी न लेना ❌", "কখনো ওষুধ না খাওয়া ❌", "হিদাক অমুক্তা চাদবা ❌", "Wat dih dawai ❌", "Ei miah loh ❌", "Dowa nakhabo ❌"),
                        getLocalizedText(lang, "Give to neighbor 👥", "ওচৰ-চুবুৰীয়াক দি দিয়া 👥", "पड़ोसी को दे देना 👥", "প্রতিবেশীকে দিয়ে দেওয়া 👥", "য়ুম্লোন্নবদা পীবা 👥", "Ai sha marjan 👥", "Thenawmte pek 👥", "Basti manu ke dibo 👥")
                    ),
                    correctIndex = 0
                ),
                GameQuestion(
                    id = "ec_3_3",
                    promptKey = getLocalizedText(lang, "Where is the best safe place to keep your reading glasses?", "পঢ়া চশমাযোৰ সুৰক্ষিতভাৱে ক'ত ৰখা উচিত?", "पढ़ने का चश्मा रखने की सबसे सुरक्षित जगह कौन सी है?", "পড়ার চশমাটি নিরাপদে রাখার সঠিক স্থান কোনটি?", "পাবগী চশমা করম্বা শেংলবা মফমদা থমগদগে?", "Hangno ban buh ia ki itshmat pule?", "Lehkha chhiarna tarmit khawi laiah nge dah him ber?", "Chasma kuntu jaga te bhal pora rakhibo?"),
                    promptTextFallback = getLocalizedText(lang, "Where is the best safe place to keep your reading glasses?", "পঢ়া চশমাযোৰ সুৰক্ষিতভাৱে ক'ত ৰখা উচিত?", "पढ़ने का चश्मा रखने की सबसे सुरक्षित जगह कौन सी है?", "পড়ার চশমাটি নিরাপদে রাখার সঠিক স্থান কোনটি?", "পাবগী চশমা করম্বা শেংলবা মফমদা থমগদগে?", "Hangno ban buh ia ki itshmat pule?", "Lehkha chhiarna tarmit khawi laiah nge dah him ber?", "Chasma kuntu jaga te bhal pora rakhibo?"),
                    visualEmoji = "👓",
                    options = listOf(
                        getLocalizedText(lang, "In its glasses case on desk 👓", "মেজৰ চশমাৰ বাকচত 👓", "मेज पर चश्मे के डिब्बे में 👓", "টেবিলে চশমার কেসে 👓", "মেথককী চশমা কেসতা 👓", "Ha ka case itshmat 👓", "Tarmit bawmah 👓", "Chasma Case te 👓"),
                        getLocalizedText(lang, "On the floor near sofa 🛋️", "সোফাৰ কাষৰ মজিয়াত 🛋️", "फर्श पर 🛋️", "মেঝেতে সোফার পাশে 🛋️", "তলদা 🛋️", "Ha madan 🛋️", "Chhuatah 🛋️", "Mati te 🛋️"),
                        getLocalizedText(lang, "Inside the refrigerator ❄️", "ফ্ৰীজৰ ভিতৰত ❄️", "फ्रिज के अंदर ❄️", "ফ্রিজের ভেতরে ❄️", "ফ্রিজ মনুংদা ❄️", "Ha fridge ❄️", "Bawm vawh chhungah ❄️", "Fridge te ❄️"),
                        getLocalizedText(lang, "In garden grass 🌱", "বাগানৰ ঘাঁহত 🌱", "बगीचे की घास में 🌱", "বাগানের ঘাসে 🌱", "লৈকোলদা 🌱", "Ha kper 🌾", "Huan hnim zingah 🌱", "Ghaas te 🌱")
                    ),
                    correctIndex = 0
                )
            )
        }
    }

    fun generateMemoryMatchCards(level: Int): List<MemoryCard> {
        val pool = listOf(
            "🌿", // Assam tea leaf
            "🦜", // Great Hornbill
            "🌸", // Rhododendron / Orchid
            "🦏", // One-horned Rhino
            "🍵", // Cup of tea
            "🔔", // Temple bell
            "🏔️", // Mountain peak
            "🏡"  // Traditional home
        )

        val pairsCount = when (level) {
            1 -> 2 // 4 cards
            2 -> 3 // 6 cards
            else -> 4 // 8 cards
        }

        val selectedEmojis = pool.take(pairsCount)
        val cards = mutableListOf<MemoryCard>()
        var idCounter = 0

        selectedEmojis.forEach { emoji ->
            cards.add(MemoryCard(id = idCounter++, content = emoji))
            cards.add(MemoryCard(id = idCounter++, content = emoji))
        }

        return cards.shuffled()
    }
}
