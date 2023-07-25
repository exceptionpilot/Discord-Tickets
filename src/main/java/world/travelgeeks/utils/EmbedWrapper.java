package world.travelgeeks.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmbedWrapper {

    public final EmbedBuilder builder;
    public final List<ActionRow> actionRow;
    public EmbedWrapper() {
        this.builder = new EmbedBuilder();
        this.actionRow = new ArrayList<>();
    }

    public EmbedWrapper setDescription(String string) {
        this.setDescription(string);
        return this;
    }

    public EmbedWrapper addField(String name, String value) {
        this.builder.addField(name, value, true);
        return this;
    }

    public EmbedWrapper addActionRow(ActionRow actionRow) {
        this.actionRow.add(actionRow);
        return this;
    }

    public void send(ButtonInteractionEvent buttonInteractionEvent) {
        this.builder.setColor(Color.decode("#D0F7F4"));
        this.builder.setFooter(buttonInteractionEvent.getGuild().getName(), buttonInteractionEvent.getGuild().getIconUrl());
        buttonInteractionEvent.replyEmbeds(this.builder.build()).addActionRow((Button) this.actionRow).queue();
    }

    public void send(TextChannel channel, String header) {
        this.builder.setColor(Color.decode("#D0F7F4"));
        this.builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
        channel.sendMessage(header).addEmbeds(this.builder.build()).addActionRow((Button) this.actionRow).queue();
    }

    public void send(TextChannel channel) {
        this.builder.setColor(Color.decode("#D0F7F4"));
        this.builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
        channel.sendMessageEmbeds(this.builder.build()).addActionRow((Button) this.actionRow).queue();
    }
}
