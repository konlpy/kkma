# KKMA Morpheme Analyzer

This is a decompiled version of [KKMA](http://kkma.snu.ac.kr) v2.0.

Source codes were extracted from [org.snu.ids.ha.2.0.jar](org.snu.ids.ha.2.0.jar) that were provided under a GPL v2 license.
<br>
Please note however, that though this repo may be useful in navigating through source codes, it is not the original source file.
To employ KKMA in your code, use [org.snu.ids.ha.2.0.jar](org.snu.ids.ha.2.0.jar) directly, and link this source to it.

For the source code of KKMA v1.0, see [here](http://github.com/serialx/kkma).

## Dictionary files

    $ tree ./dic
    .
    ├── ecat                            # Dicts used for Keyword extraction
    │   ├── ChemFormula.dic             # Chemical formulas
    │   ├── CompNoun.dic
    │   ├── JunkWord.dic
    │   ├── UOM.dic                     # Unit of Measures
    │   ├── VerbJunkWord.dic
    │   └── VerbNoun.dic
    ├── prob
    │   ├── lnpr_morp.dic
    │   ├── lnpr_pos.dic
    │   ├── lnpr_pos_g_exp.dic
    │   ├── lnpr_pos_g_morp_inter.dic
    │   ├── lnpr_pos_g_morp_intra.dic
    │   ├── lnpr_syllable_bi.dic
    │   └── lnpr_syllable_uni_noun.dic
    ├── kcc.dic
    ├── noun.dic
    ├── person.dic
    ├── raw.dic
    ├── simple.dic
    └── verb.dic


## License

[GNU GPL v2.0](http://www.gnu.org/licenses/gpl-2.0.html)
