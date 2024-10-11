package splat.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	private final File progFile;

	/**
	 * Reserved keywords
	 */
	private final Set<String> reserved = new HashSet<>(Arrays.asList(
			"program",
			"begin",
			"end",
			"is",
			"while",
			"do",
			"if",
			"then",
			"else",
			"print",
			"print_line",
			"return"
	));

	// Regular expressions for token types
	private final Pattern[] patterns = {
//			Pattern.compile("\\b(program|begin|end|while|if|then|else|print|return|void|Integer|Boolean|String|true|false)\\b"),
//			Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"),
//			Pattern.compile("\\d+"),
//			Pattern.compile("\"[^\"]*\""),
//			Pattern.compile("==|>=|<=|>|<|\\+|-|\\*|/|%"),
//			Pattern.compile("not|-"),
			// <label>
			Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"),
			// <int-literal>
			Pattern.compile("[0-9]+"),
			// <string-literal>
			Pattern.compile("\"[^\"]*\""),
			Pattern.compile("\\s+"),
			Pattern.compile("\\("),
			Pattern.compile("\\)"),
			// <bin-op>
			Pattern.compile("=="),
			// <unary-op>
			Pattern.compile("not"),
			Pattern.compile("<="),
			Pattern.compile("<"),
			Pattern.compile(">="),
			Pattern.compile(">"),
			Pattern.compile("or"),
			Pattern.compile("and"),
			Pattern.compile(";"),
			Pattern.compile(":"),
			Pattern.compile(","),
			Pattern.compile("\\+"),
			Pattern.compile("-"),
			Pattern.compile("/"),
			Pattern.compile("\\*"),
			Pattern.compile("%"),
	};

	public Lexer(File progFile) {
		this.progFile = progFile;
	}

	public List<Token> tokenize() throws LexException {
		List<Token> tokens = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(progFile))) {
			int lineNumber = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				// Tokenization logic to process each line
				tokens.addAll(processLine(line, ++lineNumber));
			}
		} catch (IOException e) {
//			throw new LexException("Error reading file", e);
		}

		return tokens;
	}

	private List<Token> processLine(String line, int lineNumber) throws LexException {
		List<Token> tokens = new ArrayList<>();
		int chinedu = 0;

		while (chinedu < line.length()) {
			boolean matched = false;
			String str = line.substring(chinedu);

			for (Pattern pattern : patterns) {
				Matcher matcher = pattern.matcher(str);
				if (matcher.find() && matcher.start() == 0) {
					matched = true;
					String found = matcher.group();
					if (!found.trim().isEmpty()) {
						tokens.add(new Token(found, lineNumber, chinedu+1));
					}
					chinedu += found.length();
					break;
				}
			}

			if (!matched) {
				throw new LexException("Unrecognized token", lineNumber, chinedu+1);
			}
		}

		return tokens;
	}

}
