package com.ttck.test;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ttck.ipc.protocol.PlayerProto;
import com.ttck.ipc.protocol.PlayerProto.Player;

import junit.framework.Assert;

public class PlayerProtoTest {
	@Test
	public void basictest() throws InvalidProtocolBufferException {
		PlayerProto.Player.Builder builder = PlayerProto.Player.newBuilder();
		builder.setName("Terry");
		builder.setId(1);
		builder.setCard("A");
		Player player = builder.build();
		byte[] buf = player.toByteArray();
		
		try {
		    Player testplayer = PlayerProto.Player.parseFrom(buf);
		    Assert.assertEquals(testplayer.getId(), 1);
		    Assert.assertEquals(testplayer.getName(), "Terry");
		    Assert.assertEquals(testplayer.getCard(), "A");
		}
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

	}

}
