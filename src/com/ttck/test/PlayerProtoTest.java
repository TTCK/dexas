package com.ttck.test;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ttck.ipc.protocol.PlayerProto;

import junit.framework.Assert;

public class PlayerProtoTest {
	@Test
	public void basictest() throws InvalidProtocolBufferException {
		PlayerProto.CreatePlayerRequest.Builder builder = PlayerProto.CreatePlayerRequest.newBuilder();
		builder.setName("Terry");
		builder.setId(1);
		PlayerProto.CreatePlayerRequest createPlayerReq = builder.build();
		byte[] buf = createPlayerReq.toByteArray();
		
		try {
			PlayerProto.CreatePlayerRequest testplayer = PlayerProto.CreatePlayerRequest.parseFrom(buf);
		    Assert.assertEquals(testplayer.getId(), 1);
		    Assert.assertEquals(testplayer.getName(), "Terry");
		}
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

	}

}
