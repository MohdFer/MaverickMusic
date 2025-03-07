package it.vfsfitvnm.vimusic.enums

enum class Languages {
    System,
    Arabic,
    Bashkir,
    Catalan,
    English,
    Esperanto,
    ChineseSimplified,
    ChineseTraditional,
    Czech,
    French,
    //FrenchEmo,
    German,
    Greek,
    Hebrew,
    Hungarian,
    Italian,
    Indonesian,
    Korean,
    Odia,
    Persian,
    Polish,
    PortugueseBrazilian,
    Portuguese,
    Romanian,
    //RomanianEmo,
    Russian,
    Spanish,
    Turkish,
    Ukrainian;

    var code: String = "en"
        get() = when (this) {
            System -> "system"
            Arabic -> "ar"
            Bashkir -> "ba"
            Catalan -> "ca"
            ChineseSimplified -> "zh-CN"
            ChineseTraditional -> "zh-TW"
            English -> "en"
            Esperanto -> "eo"
            Italian -> "it"
            Indonesian -> "in"
            Korean -> "ko"
            Czech -> "cs"
            German -> "de"
            Greek -> "el"
            Hebrew -> "he"
            Hungarian -> "hu"
            Spanish -> "es"
            French -> "fr"
            //FrenchEmo -> "fr-FR"
            Odia -> "or"
            Persian -> "fa"
            Polish -> "pl"
            Portuguese -> "pt"
            PortugueseBrazilian -> "pt-BR"
            Romanian -> "ro"
            //RomanianEmo -> "ro-RO"
            Russian -> "ru"
            Turkish -> "tr"
            Ukrainian -> "uk"
        }


}