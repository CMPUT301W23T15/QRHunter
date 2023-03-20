/**
 * This class represents a QR code object, with a hash value and a score based on the code's content.
 */
package com.goblin.qrhunter;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class represents a QR code object, which contains a hash and a score.
 *
 * The hash is calculated from the QR code string using SHA-256 algorithm, and the score is calculated
 *
 * from the hash using a custom scoring system.
 */
public class QRCode implements Serializable {

    /**
     * The hash value of the QR code.
     */
    private String hash;

    /**
     * required by firebase
     */
    public QRCode() {

    }

    /**
     * Constructs a QR code object with the given code string.
     *
     * @param code The code string used to construct the QR code.
     */
    public QRCode(String code) {
        try {
            hash = toHexString(MessageDigest.getInstance("SHA-256").digest(code.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            // pass
        }
    }

    /**
     * Returns the hash value of the QR code.
     *
     * @return The hash value of the QR code.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash value of the QR code.
     *
     * @param hash The hash value to set.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Returns the score of the QR code.
     *
     * @return The score of the QR code.
     */
    public int getScore() {
        //TODO: calculate score
        return calculateScore(hash);
    }

    /**
     * Calculates the score of the QR code based on its hash value.
     *
     * @param hex The hash value of the QR code.
     * @return The calculated score of the QR code.
     */
    public static int calculateScore(String hex) {
        // TODO: determine if should be in post or qrcode
        int score = 0;
        // find repeat characters
        Matcher matcher = Pattern.compile("(.)\\1+").matcher(hex);

        // Iter over each group of repeats
        while (matcher.find()) {
            int repeats = matcher.group().length();
            char c = matcher.group().charAt(0);
            // special case 0, else use the hex equivalent value
            int base = (c == '0') ? 20 : Character.digit(c, 16);
            // if base is less than 0, invalid hex just ignore.
            if( base > -1) {
                score += Math.pow(base, repeats - 1);
            }
        }
        return score;
    }

    /**
     * Generates a random nam that is unique to the hash using the lists.
     *
     * @return the generated name.
     */
    public String NameGenerator() {
        List<String> colors = Arrays.asList("Red", "Blue", "Green", "Yellow", "Purple", "Orange",
                "Pink", "Gray", "Brown", "Black", "White", "Navy", "Teal", "Magenta", "Cyan",
                "Lavender", "Olive", "Maroon", "Turquoise", "Beige", "Indigo", "Slate", "Silver",
                "Gold", "Bronze", "Lilac", "Burgundy", "Amethyst", "Coral", "Crimson");

        List<String> adjectives = Arrays.asList("Amicable", "Aromatic", "Affluent", "Authentic",
                "Amusing", "Amazing", "Beautiful", "Bold", "Basic", "Brilliant", "Brave", "Cosy",
                "Creamy", "Chaotic", "Daring", "Dusty", "Dazzling", "Explosive", "Elegant", "Fancy",
                "Fantastic", "Fearless", "Gracious", "Glorious", "Gigantic", "Haunting", "Hearty",
                "Happy", "Huge", "Icy", "Jubilant", "Joyful", "Lovable", "Loyal", "Likeable",
                "Mythical", "Magnificent", "Majestic", "Noble", "Peculiar", "Pure", "Perfect",
                "Quirky", "Quiet", "Quick", "Radiant", "Repulsive", "Robust", "Rich", "Safe", "Strong",
                "Super", "Stylish", "Thin", "Thrilling", "Unique", "Ultra", "Vintage", "Vibrant", "Warm",
                "Wonderful", "Weary", "Wild","Young", "Zealous");

        List<String> pokemons = Arrays.asList("Bulbasaur", "Ivysaur", "Venusaur", "Charmander",
                "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie",
                "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto",
                "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu",
                "Raichu", "Sandshrew", "Sandslash", "Nidoran", "Nidorina", "Nidoqueen", "Nidorino",
                "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff",
                "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat",
                "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck",
                "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath",
                "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout",
                "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem",
                "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch’d",
                "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster",
                "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler",
                "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee",
                "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey",
                "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu",
                "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir",
                "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon",
                "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl",
                "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite",
                "Mewtwo", "Mew", "Chikorita", "Bayleef", "Meganium", "Cyndaquil", "Quilava",
                "Typhlosion", "Totodile", "Croconaw", "Feraligatr", "Sentret", "Furret", "Hoothoot",
                "Noctowl", "Ledyba", "Ledian", "Spinarak", "Ariados", "Crobat", "Chinchou", "Lanturn",
                "Pichu", "Cleffa", "Igglybuff", "Togepi", "Togetic", "Natu", "Xatu", "Mareep",
                "Flaaffy", "Ampharos", "Bellossom", "Marill", "Azumarill", "Sudowoodo", "Politoed",
                "Hoppip", "Skiploom", "Jumpluff", "Aipom", "Sunkern", "Sunflora", "Yanma", "Wooper",
                "Quagsire", "Espeon", "Umbreon", "Murkrow", "Slowking", "Misdreavus", "Unown",
                "Wobbuffet", "Girafarig", "Pineco", "Forretress", "Dunsparce", "Gligar", "Steelix",
                "Snubbull", "Granbull", "Qwilfish", "Scizor", "Shuckle", "Heracross", "Sneasel",
                "Teddiursa", "Ursaring", "Slugma", "Magcargo", "Swinub", "Piloswine", "Corsola",
                "Remoraid", "Octillery", "Delibird", "Mantine", "Skarmory", "Houndour", "Houndoom",
                "Kingdra", "Phanpy", "Donphan", "Porygon2", "Stantler", "Smeargle", "Tyrogue",
                "Hitmontop", "Smoochum", "Elekid", "Magby", "Miltank", "Blissey", "Raikou", "Entei",
                "Suicune", "Larvitar", "Pupitar", "Tyranitar", "Lugia", "Ho-Oh", "Celebi", "Treecko",
                "Grovyle", "Sceptile", "Torchic", "Combusken", "Blaziken", "Mudkip", "Marshtomp",
                "Swampert", "Poochyena", "Mightyena", "Zigzagoon", "Linoone", "Wurmple", "Silcoon",
                "Beautifly", "Cascoon", "Dustox", "Lotad", "Lombre", "Ludicolo", "Seedot", "Nuzleaf",
                "Shiftry", "Taillow", "Swellow", "Wingull", "Pelipper", "Ralts", "Kirlia", "Gardevoir",
                "Surskit", "Masquerain", "Shroomish", "Breloom", "Slakoth", "Vigoroth", "Slaking",
                "Nincada", "Ninjask", "Shedinja", "Whismur", "Loudred", "Exploud", "Makuhita",
                "Hariyama", "Azurill", "Nosepass", "Skitty", "Delcatty", "Sableye", "Mawile", "Aron",
                "Lairon", "Aggron", "Meditite", "Medicham", "Electrike", "Manectric", "Plusle",
                "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Swalot", "Carvanha", "Sharpedo",
                "Wailmer", "Wailord", "Numel", "Camerupt", "Torkoal", "Spoink", "Grumpig", "Spinda",
                "Trapinch", "Vibrava", "Flygon", "Cacnea", "Cacturne", "Swablu", "Altaria", "Zangoose",
                "Seviper", "Lunatone", "Solrock", "Barboach", "Whiscash", "Corphish", "Crawdaunt",
                "Baltoy", "Claydol", "Lileep", "Cradily", "Anorith", "Armaldo", "Feebas", "Milotic",
                "Castform", "Kecleon", "Shuppet", "Banette", "Duskull", "Dusclops", "Tropius",
                "Chimecho", "Absol", "Wynaut", "Snorunt", "Glalie", "Spheal", "Sealeo", "Walrein",
                "Clamperl", "Huntail", "Gorebyss", "Relicanth", "Luvdisc", "Bagon", "Shelgon",
                "Salamence", "Beldum", "Metang", "Metagross", "Regirock", "Regice", "Registeel",
                "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Jirachi", "Deoxys", "Turtwig",
                "Grotle", "Torterra", "Chimchar", "Monferno", "Infernape", "Piplup", "Prinplup",
                "Empoleon", "Starly", "Staravia", "Staraptor", "Bidoof", "Bibarel", "Kricketot",
                "Kricketune", "Shinx", "Luxio", "Luxray", "Budew", "Roserade", "Cranidos", "Rampardos",
                "Shieldon", "Bastiodon", "Burmy", "Wormadam", "Mothim", "Combee", "Vespiquen",
                "Pachirisu", "Buizel", "Floatzel", "Cherubi", "Cherrim", "Shellos", "Gastrodon",
                "Ambipom", "Drifloon", "Drifblim", "Buneary", "Lopunny", "Mismagius", "Honchkrow",
                "Glameow", "Purugly", "Chingling", "Stunky", "Skuntank", "Bronzor", "Bronzong",
                "Bonsly", "Mime Jr.", "Happiny", "Chatot", "Spiritomb", "Gible", "Gabite", "Garchomp",
                "Munchlax", "Riolu", "Lucario", "Hippopotas", "Hippowdon", "Skorupi", "Drapion",
                "Croagunk", "Toxicroak", "Carnivine", "Finneon", "Lumineon", "Mantyke", "Snover",
                "Abomasnow", "Weavile", "Magnezone", "Lickilicky", "Rhyperior", "Tangrowth",
                "Electivire", "Magmortar", "Togekiss", "Yanmega", "Leafeon", "Glaceon", "Gliscor",
                "Mamoswine", "Porygon-Z", "Gallade", "Probopass", "Dusknoir", "Froslass", "Rotom",
                "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina",
                "Cresselia", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus", "Victini", "Snivy",
                "Servine", "Serperior", "Tepig", "Pignite", "Emboar", "Oshawott", "Dewott", "Samurott",
                "Patrat", "Watchog", "Lillipup", "Herdier", "Stoutland", "Purrloin", "Liepard",
                "Pansage", "Simisage", "Pansear", "Simisear", "Panpour", "Simipour", "Munna",
                "Musharna", "Pidove", "Tranquill", "Unfezant", "Blitzle", "Zebstrika", "Roggenrola",
                "Boldore", "Gigalith", "Woobat", "Swoobat", "Drilbur", "Excadrill", "Audino",
                "Timburr", "Gurdurr", "Conkeldurr", "Tympole", "Palpitoad", "Seismitoad", "Throh",
                "Sawk", "Sewaddle", "Swadloon", "Leavanny", "Venipede", "Whirlipede", "Scolipede",
                "Cottonee", "Whimsicott", "Petilil", "Lilligant", "Basculin", "Sandile", "Krokorok",
                "Krookodile", "Darumaka", "Darmanitan", "Maractus", "Dwebble", "Crustle", "Scraggy",
                "Scrafty", "Sigilyph", "Yamask", "Cofagrigus", "Tirtouga", "Carracosta", "Archen",
                "Archeops", "Trubbish", "Garbodor", "Zorua", "Zoroark", "Minccino", "Cinccino",
                "Gothita", "Gothorita", "Gothitelle", "Solosis", "Duosion", "Reuniclus", "Ducklett",
                "Swanna", "Vanillite", "Vanillish", "Vanilluxe", "Deerling", "Sawsbuck", "Emolga",
                "Karrablast", "Escavalier", "Foongus", "Amoonguss", "Frillish", "Jellicent", "Alomomola",
                "Joltik", "Galvantula", "Ferroseed", "Ferrothorn", "Klink", "Klang", "Klinklang",
                "Tynamo", "Eelektrik", "Eelektross", "Elgyem", "Beheeyem", "Litwick", "Lampent",
                "Chandelure", "Axew", "Fraxure", "Haxorus", "Cubchoo", "Beartic", "Cryogonal",
                "Shelmet", "Accelgor", "Stunfisk", "Mienfoo", "Mienshao", "Druddigon", "Golett",
                "Golurk", "Pawniard", "Bisharp", "Bouffalant", "Rufflet", "Braviary", "Vullaby",
                "Mandibuzz", "Heatmor", "Durant", "Deino", "Zweilous", "Hydreigon", "Larvesta",
                "Volcarona", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram",
                "Zekrom", "Landorus", "Kyurem", "Keldeo", "Meloetta", "Genesect", "Chespin", "Quilladin",
                "Chesnaught", "Fennekin", "Braixen", "Delphox", "Froakie", "Frogadier", "Greninja",
                "Bunnelby", "Diggersby", "Fletchling", "Fletchinder", "Talonflame", "Scatterbug",
                "Spewpa", "Vivillon", "Litleo", "Pyroar", "Flabebe", "Floette", "Florges", "Skiddo",
                "Gogoat", "Pancham", "Pangoro", "Furfrou", "Espurr", "Meowstic", "Honedge", "Doublade",
                "Aegislash", "Spritzee", "Aromatisse", "Swirlix", "Slurpuff", "Inkay", "Malamar",
                "Binacle", "Barbaracle", "Skrelp", "Dragalge", "Clauncher", "Clawitzer", "Helioptile",
                "Heliolisk", "Tyrunt", "Tyrantrum", "Amaura", "Aurorus", "Sylveon", "Hawlucha",
                "Dedenne", "Carbink", "Goomy", "Sliggoo", "Goodra", "Klefki", "Phantump", "Trevenant",
                "Pumpkaboo", "Gourgeist", "Bergmite", "Avalugg", "Noibat", "Noivern", "Xerneas",
                "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion", "Rowlet", "Dartrix", "Decidueye",
                "Litten", "Torracat", "Incineroar", "Popplio", "Brionne", "Primarina", "Pikipek",
                "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Grubbin", "Charjabug", "Vikavolt",
                "Crabrawler", "Crabominable", "Oricorio", "Cutiefly", "Ribombee", "Rockruff", "Lycanroc",
                "Wishiwashi", "Mareanie", "Toxapex", "Mudbray", "Mudsdale", "Dewpider", "Araquanid",
                "Fomantis", "Lurantis", "Morelull", "Shiinotic", "Salandit", "Salazzle", "Stufful",
                "Bewear", "Bounsweet", "Steenee", "Tsareena", "Comfey", "Oranguru", "Passimian",
                "Wimpod", "Golisopod", "Sandygast", "Palossand", "Pyukumuku", "Silvally", "Minior",
                "Komala", "Turtonator", "Togedemaru", "Mimikyu", "Bruxish", "Drampa", "Dhelmise",
                "Jangmo-o", "Hakamo-o", "Kommo-o", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini",
                "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa",
                "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Necrozma", "Magearna", "Marshadow",
                "Poipole", "Naganadel", "Stakataka", "Blacephalon", "Zeraora", "Meltan", "Melmetal",
                "Grookey", "Thwackey", "Rillaboom", "Scorbunny", "Raboot", "Cinderace", "Sobble",
                "Drizzile", "Inteleon", "Skwovet", "Greedent", "Rookidee", "Corvisquire", "Corviknight",
                "Blipbug", "Dottler", "Orbeetle", "Nickit", "Thievul", "Gossifleur", "Eldegoss",
                "Wooloo", "Dubwool", "Chewtle", "Drednaw", "Yamper", "Boltund", "Rolycoly", "Carkol",
                "Coalossal", "Applin", "Flapple", "Appletun", "Silicobra", "Sandaconda", "Cramorant",
                "Arrokuda", "Barraskewda", "Toxel", "Toxtricity", "Sizzlipede", "Centiskorch", "Clobbopus",
                "Grapploct", "Sinistea", "Polteageist", "Hatenna", "Hattrem", "Hatterene", "Impidimp",
                "Morgrem", "Grimmsnarl", "Obstagoon", "Perrserker", "Cursola", "Sirfetch'd", "Mr. Rime",
                "Runerigus", "Milcery", "Alcremie", "Falinks", "Pincurchin", "Snom", "Frosmoth",
                "Stonjourner", "Eiscue", "Indeedee", "Morpeko", "Cufant", "Copperajah", "Dracozolt",
                "Arctozolt", "Dracovish", "Arctovish", "Duraludon", "Dreepy", "Drakloak", "Dragapult",
                "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude", "Regieleki", "Regidrago",
                "Glastrier", "Spectrier", "Calyrex", "Wyrdeer", "Kleavor", "Ursaluna", "Basculegion",
                "Sneasler", "Overqwil", "Enamorus");

        int index1 = (Integer.parseInt(hash.substring(0, 8), 16) % colors.size());
        int index2 = (Integer.parseInt(hash.substring(8, 16), 16) % adjectives.size());
        int index3 = (Integer.parseInt(hash.substring(16, 24), 16) % pokemons.size());

        String color = colors.get(index1);
        String adjective = adjectives.get(index2);
        String pokemon = pokemons.get(index3);

        String qrName = adjective + " " + color + " " + pokemon;
        return qrName;

    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    @Exclude
    public static String toHexString(byte[] bytes) {
        // https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l/2197650#2197650
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof QRCode)) {
            return false;
        }

        QRCode other = (QRCode) obj;

        return this.hash.equals(other.hash);
    }
}

