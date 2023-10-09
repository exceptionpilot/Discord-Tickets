package world.travelgeeks.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;

import javax.sound.midi.SysexMessage;
import java.lang.reflect.Member;

public class ReplaceWrapper {

    private final Member member;
    private final Member targetMember;
    private final User user;
    private final User targetUser;
    private final Channel channel;
    private final Permission permission;
    private final String topic;
    private final String error;

    public ReplaceWrapper(Member member, Member targetMember, User user, User targetUser, Channel channel, Permission permission, String topic, String error) {
        this.member = member;
        this.targetMember = targetMember;
        this.user = user;
        this.targetUser = targetUser;
        this.channel = channel;
        this.permission = permission;
        this.topic = topic;
        this.error = error;
    }
}
