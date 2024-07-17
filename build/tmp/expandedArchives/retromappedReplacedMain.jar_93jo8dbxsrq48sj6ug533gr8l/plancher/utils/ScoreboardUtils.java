package plancher.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;

public class ScoreboardUtils {
	public static boolean scoreboardContains(String string) {
		for (String line : getScoreboard()) {
			if (removeFormatting(cleanSB(line)).contains(string)) {
				return true;
			}
		}
		return false;
	}

	public static boolean scoreboardContains(String string, List<String> scoreboard) {
		for (String line : scoreboard) {
			if (removeFormatting(cleanSB(line)).contains(string)) {
				return true;
			}
		}
		return false;
	}

	public static String cleanSB(String scoreboard) {
		char[] nvString = removeFormatting(scoreboard).toCharArray();
		StringBuilder cleaned = new StringBuilder();

		for (char c : nvString) {
			if ((int) c > 20 && (int) c < 127) {
				cleaned.append(c);
			}
		}

		return cleaned.toString();
	}

	@SuppressWarnings({ "ExecutionException", "IllegalArgumentException" })
	public static List<String> getScoreboard() {
		Minecraft mc = Minecraft.func_71410_x();
		List<String> lines = new ArrayList<>();
		if (mc.field_71441_e == null)
			return lines;
		Scoreboard scoreboard = mc.field_71441_e.func_96441_U();
		if (scoreboard == null)
			return lines;

		ScoreObjective objective = scoreboard.func_96539_a(1);
		if (objective == null)
			return lines;

		Collection<Score> scores = scoreboard.func_96534_i(objective);
		List<Score> list = scores.stream().filter(
				input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#"))
				.collect(Collectors.toList());

		if (list.size() > 15) {
			scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
		} else {
			scores = list;
		}

		for (Score score : scores) {
			ScorePlayerTeam team = scoreboard.func_96509_i(score.func_96653_e());
			lines.add(ScorePlayerTeam.func_96667_a(team, score.func_96653_e()));
		}

		return lines;
	}

	public static String removeFormatting(String input) {
		return input.replaceAll("�[0-9a-fk-or]", "");
	}
}
