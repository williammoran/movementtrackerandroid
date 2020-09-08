package com.potentialtech.movementtracker

interface LocationGatherer {
    fun registerReceiver(receiver: LocationReceiver)
    fun start()
}