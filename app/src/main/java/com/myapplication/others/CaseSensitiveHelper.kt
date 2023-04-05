package com.myapplication.others

object CaseSensitiveHelper {

    fun returnCamelCaseWord(str:String): String {
        val words: List<String> = str.split(" ")
        val sb = StringBuilder()
        if (words[0].length > 0) {
            sb.append(
                Character.toUpperCase(words[0][0]).toString() + words[0].subSequence(
                    1,
                    words[0].length
                ).toString().toLowerCase()
            )
            for (i in 1 until words.size) {
                sb.append(" ")
                sb.append(
                    Character.toUpperCase(words[i][0]).toString() + words[i].subSequence(
                        1,
                        words[i].length
                    ).toString().toLowerCase()
                )
            }
        }

        return sb.toString()
    }
}