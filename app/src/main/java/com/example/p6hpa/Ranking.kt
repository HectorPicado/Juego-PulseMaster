package com.example.p6hpa

object Ranking {
    var ranking = mutableListOf<Jugador>()

    fun rankingFiltrado(tipo: String): List<Jugador> {

        if (tipo == "GENERAL") {
            return ranking.sortedByDescending { it.puntos }
        }

        val listaFiltrada = mutableListOf<Jugador>()

        for (jugador in ranking) {
            when (tipo) {
                "DIARIO" -> {
                    if (jugador.fecha == 1) listaFiltrada.add(jugador)
                }
                "SEMANAL" -> {
                    if (jugador.fecha <= 7) listaFiltrada.add(jugador)
                }
            }
        }

        return listaFiltrada.sortedByDescending { it.puntos }
    }
}