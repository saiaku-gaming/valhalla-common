package com.valhallagame.common.rabbitmq;

public class RabbitMQRouting {
	public enum Exchange {
		PERSON, FRIEND, PARTY, CHAT, WARDROBE, INSTANCE_CONTAINER, INSTANCE, CHARACTER
	}

	public enum InstanceContainer {
		CREATE, READY
	}

	public enum Instance {
		DUNGEON_ACTIVE, PERSON_LOGIN, PERSON_LOGOUT
	}

	public enum Person {
		DELETE, CREATE, ONLINE, OFFLINE
	}

	public enum Friend {
		ADD, REMOVE, RECEIVED_INVITE, DECLINE_INVITE
	}

	public enum Party {
		CANCEL_INVITE, ACCEPT_INVITE, DECLINE_INVITE, LEAVE, PROMOTE_LEADER, KICK_FROM_PARTY, SENT_INVITE, RECEIVED_INVITE
	}

	public enum Chat {
		RECEIVED_MESSAGE
	}

	public enum Wardrobe {
		ADD_WARDROBE_ITEM
	}

	public enum Character {
		DELETE
	}
}
