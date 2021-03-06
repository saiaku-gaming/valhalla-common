package com.valhallagame.common.rabbitmq;

public class RabbitMQRouting {
	public enum Exchange {
        PERSON, FRIEND, PARTY, CHAT, WARDROBE, INSTANCE_CONTAINER, INSTANCE, CHARACTER, FEAT, STATISTICS, TRAIT, RECIPE, CURRENCY
	}

	public enum InstanceContainer {
		CREATE, READY
	}

	public enum Instance {
		DUNGEON_ACTIVE, PERSON_LOGIN, PERSON_LOGOUT, DUNGEON_QUEUED, DUNGEON_FINISHING, DUNGEON_FINISHED, QUEUE_PLACEMENT_FULFILLED
	}

	public enum Person {
        DELETE, CREATE, ONLINE, OFFLINE, DISPLAYNAME_CHANGE
	}

	public enum Friend {
        ADD, REMOVE, RECEIVED_INVITE, DECLINE_INVITE, SENT_INVITE, CANCEL_INVITE, ONLINE, OFFLINE
	}

	public enum Party {
		CANCEL_INVITE, ACCEPT_INVITE, DECLINE_INVITE, LEAVE, PROMOTE_LEADER, KICK_FROM_PARTY, SENT_INVITE, RECEIVED_INVITE, SELECT_CHARACTER, PERSON_OFFLINE, PERSON_ONLINE, CREATED
	}

	public enum Chat {
		RECEIVED_MESSAGE
	}

	public enum Wardrobe {
		ADD_WARDROBE_ITEM, REMOVE_WARDROBE_ITEM
	}

	public enum Feat {
		ADD, REMOVE
	}

	public enum Character {
		DELETE, SELECT
	}

	public enum Statistics {
		INT_COUNTER, LOW_TIMER, HIGH_TIMER
	}

	public enum Trait {
		UNLOCK, LOCK
	}

    public enum Recipe {
        ADD, REMOVE
    }

	public enum Currency {
		ADD, REMOVE
	}
}
