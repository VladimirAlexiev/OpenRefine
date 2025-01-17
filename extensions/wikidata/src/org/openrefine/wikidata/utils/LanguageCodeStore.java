
package org.openrefine.wikidata.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * A store for the allowed language code for terms and monolingual text values in Wikibase.
 *
 * TODO: separate the languages allowed for terms from the ones allowed for monolingual text. Currently the list is for
 * monolingual texts (which is larger).
 * 
 * Query to update this list: curl
 * "https://www.wikidata.org/w/api.php?action=query&amp;meta=wbcontentlanguages&amp;wbclprop=code&amp;wbclcontext=monolingualtext&amp;format=json"
 * | jq ".query.wbcontentlanguages[].code" | sed -e "s/$/,/"
 * 
 * @author Antonin Delpeuch
 *
 */
public class LanguageCodeStore {

    private static final Logger logger = LoggerFactory.getLogger(LanguageCodeStore.class);

    private static Map<String, Set<String>> apiEndpointToLangCodes = new HashMap<>();

    public static Set<String> getLanguageCodes(String mediaWikiApiEndpoint) {
        if (mediaWikiApiEndpoint == null) return DEFAULT_LANGUAGE_CODES;

        if (apiEndpointToLangCodes.containsKey(mediaWikiApiEndpoint)) {
            return apiEndpointToLangCodes.get(mediaWikiApiEndpoint);
        }

        try {
            Set<String> langCodes = fetchLangCodes(mediaWikiApiEndpoint);
            apiEndpointToLangCodes.put(mediaWikiApiEndpoint, langCodes);
        } catch (IOException e) {
            logger.error("An error occurred when fetching language codes from: "
                    + mediaWikiApiEndpoint + ", fall back to the default language codes", e);
            apiEndpointToLangCodes.put(mediaWikiApiEndpoint, DEFAULT_LANGUAGE_CODES);
        }
        return apiEndpointToLangCodes.get(mediaWikiApiEndpoint);
    }

    private static Set<String> fetchLangCodes(String mediaWikiApiEndpoint) throws IOException {
        String url = mediaWikiApiEndpoint +
                "?action=query&meta=wbcontentlanguages&wbclprop=code&wbclcontext=monolingualtext&format=json";
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
        JsonNode languages = jsonNode.path("query").path("wbcontentlanguages");
        Set<String> supportedLangCodes = new HashSet<>();
        for (JsonNode language : languages) {
            supportedLangCodes.add(language.path("code").textValue());
        }

        return supportedLangCodes;
    }

    private static Set<String> DEFAULT_LANGUAGE_CODES = new HashSet<>(Arrays.asList(
            "aa",
            "ab",
            "abs",
            "ace",
            "ady",
            "ady-cyrl",
            "aeb",
            "aeb-arab",
            "aeb-latn",
            "af",
            "ak",
            "aln",
            "als",
            "am",
            "an",
            "ang",
            "anp",
            "ar",
            "arc",
            "arn",
            "arq",
            "ary",
            "arz",
            "as",
            "ase",
            "ast",
            "atj",
            "av",
            "avk",
            "awa",
            "ay",
            "az",
            "azb",
            "ba",
            "ban",
            "bar",
            "bbc",
            "bbc-latn",
            "bcc",
            "bcl",
            "be",
            "be-tarask",
            "bg",
            "bgn",
            "bh",
            "bho",
            "bi",
            "bjn",
            "bm",
            "bn",
            "bo",
            "bpy",
            "bqi",
            "br",
            "brh",
            "bs",
            "btm",
            "bto",
            "bug",
            "bxr",
            "ca",
            "cbk-zam",
            "cdo",
            "ce",
            "ceb",
            "ch",
            "cho",
            "chr",
            "chy",
            "ckb",
            "co",
            "cps",
            "cr",
            "crh",
            "crh-cyrl",
            "crh-latn",
            "cs",
            "csb",
            "cu",
            "cv",
            "cy",
            "da",
            "de",
            "de-at",
            "de-ch",
            "din",
            "diq",
            "dsb",
            "dtp",
            "dty",
            "dv",
            "dz",
            "ee",
            "egl",
            "el",
            "eml",
            "en",
            "en-ca",
            "en-gb",
            "eo",
            "es",
            "es-419",
            "et",
            "eu",
            "ext",
            "fa",
            "ff",
            "fi",
            "fit",
            "fj",
            "fo",
            "fr",
            "frc",
            "frp",
            "frr",
            "fur",
            "fy",
            "ga",
            "gag",
            "gan",
            "gan-hans",
            "gan-hant",
            "gcr",
            "gd",
            "gl",
            "glk",
            "gn",
            "gom",
            "gom-deva",
            "gom-latn",
            "gor",
            "got",
            "grc",
            "gsw",
            "gu",
            "gv",
            "ha",
            "hak",
            "haw",
            "he",
            "hi",
            "hif",
            "hif-latn",
            "hil",
            "ho",
            "hr",
            "hrx",
            "hsb",
            "ht",
            "hu",
            "hy",
            "hyw",
            "hz",
            "ia",
            "id",
            "ie",
            "ig",
            "ii",
            "ik",
            "ike-cans",
            "ike-latn",
            "ilo",
            "inh",
            "io",
            "is",
            "it",
            "iu",
            "ja",
            "jam",
            "jbo",
            "jut",
            "jv",
            "ka",
            "kaa",
            "kab",
            "kbd",
            "kbd-cyrl",
            "kbp",
            "kea",
            "kg",
            "khw",
            "ki",
            "kiu",
            "kj",
            "kjp",
            "kk",
            "kk-arab",
            "kk-cn",
            "kk-cyrl",
            "kk-kz",
            "kk-latn",
            "kk-tr",
            "kl",
            "km",
            "kn",
            "ko",
            "ko-kp",
            "koi",
            "kr",
            "krc",
            "kri",
            "krj",
            "krl",
            "ks",
            "ks-arab",
            "ks-deva",
            "ksh",
            "ku",
            "ku-arab",
            "ku-latn",
            "kum",
            "kv",
            "kw",
            "ky",
            "la",
            "lad",
            "lb",
            "lbe",
            "lez",
            "lfn",
            "lg",
            "li",
            "lij",
            "liv",
            "lki",
            "lmo",
            "ln",
            "lo",
            "loz",
            "lrc",
            "lt",
            "ltg",
            "lus",
            "luz",
            "lv",
            "lzh",
            "lzz",
            "mai",
            "map-bms",
            "mdf",
            "mg",
            "mh",
            "mhr",
            "mi",
            "min",
            "mk",
            "ml",
            "mn",
            "mni",
            "mnw",
            "mo",
            "mr",
            "mrj",
            "ms",
            "mt",
            "mus",
            "mwl",
            "my",
            "myv",
            "mzn",
            "na",
            "nah",
            "nan",
            "nap",
            "nb",
            "nds",
            "nds-nl",
            "ne",
            "new",
            "ng",
            "niu",
            "nl",
            "nn",
            "no",
            "nod",
            "nov",
            "nqo",
            "nrm",
            "nso",
            "nv",
            "ny",
            "nys",
            "oc",
            "olo",
            "om",
            "or",
            "os",
            "ota",
            "pa",
            "pag",
            "pam",
            "pap",
            "pcd",
            "pdc",
            "pdt",
            "pfl",
            "pi",
            "pih",
            "pl",
            "pms",
            "pnb",
            "pnt",
            "prg",
            "ps",
            "pt",
            "pt-br",
            "qu",
            "qug",
            "rgn",
            "rif",
            "rm",
            "rmy",
            "rn",
            "ro",
            "roa-tara",
            "ru",
            "rue",
            "rup",
            "ruq",
            "ruq-cyrl",
            "ruq-latn",
            "rw",
            "rwr",
            "sa",
            "sah",
            "sat",
            "sc",
            "scn",
            "sco",
            "sd",
            "sdc",
            "sdh",
            "se",
            "sei",
            "ses",
            "sg",
            "sgs",
            "sh",
            "shi",
            "shi-latn",
            "shi-tfng",
            "shn",
            "shy-latn",
            "si",
            "sje",
            "sk",
            "skr",
            "skr-arab",
            "sl",
            "sli",
            "sm",
            "sma",
            "smj",
            "smn",
            "sms",
            "sn",
            "so",
            "sq",
            "sr",
            "sr-ec",
            "sr-el",
            "srn",
            "srq",
            "ss",
            "st",
            "stq",
            "sty",
            "su",
            "sv",
            "sw",
            "szl",
            "ta",
            "tay",
            "tcy",
            "te",
            "tet",
            "tg",
            "tg-cyrl",
            "tg-latn",
            "th",
            "ti",
            "tk",
            "tl",
            "tly",
            "tn",
            "to",
            "tpi",
            "tr",
            "tru",
            "ts",
            "tt",
            "tt-cyrl",
            "tt-latn",
            "tum",
            "tw",
            "ty",
            "tyv",
            "tzm",
            "udm",
            "ug",
            "ug-arab",
            "ug-latn",
            "uk",
            "ur",
            "uz",
            "uz-cyrl",
            "uz-latn",
            "ve",
            "vec",
            "vep",
            "vi",
            "vls",
            "vmf",
            "vo",
            "vot",
            "vro",
            "wa",
            "war",
            "wo",
            "wuu",
            "xal",
            "xh",
            "xmf",
            "xsy",
            "yi",
            "yo",
            "yue",
            "za",
            "zea",
            "zgh",
            "zh",
            "zh-cn",
            "zh-hans",
            "zh-hant",
            "zh-hk",
            "zh-mo",
            "zh-my",
            "zh-sg",
            "zh-tw",
            "zu",
            "und",
            "mis",
            "mul",
            "zxx",
            "abe",
            "abq",
            "ami",
            "bnn",
            "brx",
            "chn",
            "cnr",
            "cop",
            "el-cy",
            "ett",
            "eya",
            "fkv",
            "fos",
            "fr-ca",
            "frm",
            "fro",
            "fuf",
            "gez",
            "gmy",
            "hai",
            "haz",
            "hbo",
            "kjh",
            "koy",
            "lag",
            "lkt",
            "lld",
            "mid",
            "mnc",
            "moe",
            "non",
            "nr",
            "nxm",
            "ood",
            "otk",
            "pjt",
            "ppu",
            "pwn",
            "pyu",
            "quc",
            "qya",
            "rar",
            "shy",
            "sia",
            "sjd",
            "sjk",
            "sjn",
            "sjt",
            "sju",
            "ssf",
            "syc",
            "tlb",
            "trv",
            "tzl",
            "uga",
            "umu",
            "uun",
            "xpu",
            "yap",
            "zun"));
}
