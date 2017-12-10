package com.valhallagame.common.rabbitmq;

public class RabbitMQRouting {
	public static enum Exchange {
		PERSON, FRIEND, PARTY, CHAT, WARDROBE, INSTANCE_CONTAINER
	}

	public static enum InstanceContainer {
		CREATE, READY
	}
	
	
	public static enum Person {
		DELETE, CREATE, ONLINE, OFFLINE
	}

	public static enum Friend {
		ADD, REMOVE, RECEIVED_INVITE, DECLINE_INVITE
	}

	public static enum Party {
		CANCEL_INVITE, ACCEPT_INVITE, DECLINE_INVITE, LEAVE, PROMOTE_LEADER, KICK_FROM_PARTY, SENT_INVITE
	}

	public static enum Chat {
		RECEIVED_MESSAGE
	}

	public static enum Wardrobe {
		ADD_WARDROBE_ITEM
	}
}
