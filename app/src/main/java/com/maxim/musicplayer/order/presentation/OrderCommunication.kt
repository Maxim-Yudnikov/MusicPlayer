package com.maxim.musicplayer.order.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface OrderCommunication: Communication.Mutable<OrderState> {
    class Base: Communication.Regular<OrderState>(), OrderCommunication
}