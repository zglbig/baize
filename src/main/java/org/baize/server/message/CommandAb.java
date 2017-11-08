package org.baize.server.message;

import io.netty.channel.Channel;
import org.baize.dao.model.CorePlayer;
import org.baize.logic.room.IRoom;

/**
 * 作者： 白泽
 * 时间： 2017/11/3.
 * 描述：
 */
public abstract class CommandAb implements ICommand {
    private int scenesId;
    private Channel ctx;
    private CorePlayer corePlayer;
    private IRoom room;


    public CommandAb() {
    }

    public CommandAb(int scenesId, Channel ctx, CorePlayer corePlayer, IRoom room) {
        this.scenesId = scenesId;
        this.ctx = ctx;
        this.corePlayer = corePlayer;
        this.room = room;
    }

    public IRoom getRoom() {
        return room;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public CorePlayer getCorePlayer() {
        return corePlayer;
    }

    public void setCorePlayer(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
    }
    public int getScenesId() {
        return scenesId;
    }

    public void setScenesId(int scenesId) {
        this.scenesId = scenesId;
    }

    public Channel getCtx() {
        return ctx;
    }

    public void setCtx(Channel ctx) {
        this.ctx = ctx;
    }
    public CorePlayer player(){
        return this.corePlayer;
    }
    public IRoom room(){
        return this.room;
    }
    public void run() {
        this.execute();
    }
}
