package com.maxim.musicplayer.media

interface ShuffleOrder {
    fun shuffle(list: List<Long>): List<Long>

    class Base: ShuffleOrder {
        override fun shuffle(list: List<Long>): List<Long> {
            return list.shuffled()
        }
    }

    class Mock: ShuffleOrder {
        override fun shuffle(list: List<Long>): List<Long> {
            val newList = mutableListOf<Long>()
            for (i in 0..(list.lastIndex / 2)) {
                newList.add(list[i * 2])
            }
            for (i in 0..(list.lastIndex / 2)) {
                newList.add(list[i * 2 + 1])
            }
            return newList
        }
    }
}