package com.valhallagame.common.rabbitmq;

public class RabbitMQRouting {
	public static enum Exchange {
		PERSON, FRIEND, PARTY
	}

	public static enum Person {
		DELETE, CREATE, ONLINE, OFFLINE
	}

	public static enum Friend {
		ADD, REMOVE, RECEIVED_INVITE, DECLINE_INVITE
	}

	public static enum Party {
		CANCEL_INVITE, ACCEPT_INVITE, DECLINE_INVITE, LEAVE
	}
}
