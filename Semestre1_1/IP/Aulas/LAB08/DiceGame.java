import java.util.Scanner;

/**
 * A classe {@code DiceGame} permite um utilizador jogar um jogo de dados muito
 * simples. Trata-se de um jogo "a feijoes".
 *
 * O utilizador diz quantos feijoes tem para jogar. A partir dai, enquanto tiver
 * algum feijao pode fazer uma aposta e, de seguida, sao rodados 2 dados. Se a
 * soma der 7, ganha o dobro do que apostou; senao perde os feijoes que apostou.
 * Para sair do jogo o utilizador so tem de apostar 0, levando nesse caso todos
 * os feijoes que tem.
 *
 *
 * Compilar: javac DiceGame.java
 * Executar: java DiceGame
 *
 * @author antonialopes IP1819
 * @author filipecasal IP1920
 * @author Miguel Nunes fc56338
 */
public class DiceGame {

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		Dice dice1 = new Dice();
		Dice dice2 = new Dice();
		String playerName = readName(reader);
		int startMoney = readValue(reader, playerName, 100);
		int totalMoney = startMoney;
		int bet;
		do {
			System.out.println();
			bet = readBet(reader, playerName, totalMoney);
			if (bet > 0) {
				totalMoney = playRound(dice1, dice2, totalMoney, bet);
			}
			else {
				showFinalResult(playerName, totalMoney - startMoney);
			}
		} while (bet > 0);
	}

	private static void showFinalResult(String playerName, int profit) {
		if (profit > 0) {
			System.out.println("Parabéns, acabaste o jogo com mais " + profit + " feijões do que tinhas no início.");
		}
		else if (profit < 0) {
			System.out.println("Infelizmente, acabaste o jogo com menos " + -profit + " feijões do que tinhas no início.");
		}
		else {
			System.out.println("Acabaste o jogo com os mesmos feijões do que tinhas no início.");
		}
	}

	static int playRound(Dice dice1, Dice dice2, int totalMoney, int bet) {
		dice1.roll();
		System.out.println("Dado 1: " + dice1.getValue());
		dice2.roll();
		System.out.println("Dado 2: " + dice2.getValue());
		int soma = dice1.getValue() + dice2.getValue();
		System.out.println("Soma dos dados: " + soma);
		if (soma == 7) {
			System.out.println("Ganhaste. Duplicaste a tua aposta. (+" + bet + ")");
			totalMoney += bet;
		}
		else {
			System.out.println("Perdeste. Perdeste a tua aposta. (-" + bet + ")");
			totalMoney -= bet;
		}
		return totalMoney;
	}

	static int readBet(Scanner reader, String playerName, int totalMoney) {
		System.out.print(playerName + ", quantos feijões queres apostar? (Se aposteres 0 o jogo acaba) ");
		return readIntInInterval(0, totalMoney, reader, "Tens que escrever um número entre 0 e os teus feijões total. Aposta: ");
	}

	static String readName(Scanner reader) {
		System.out.print("Como te chamas? ");
		return reader.nextLine();
	}

	/**
	 * Le o valor inicial com que o jogador entra no jogo (um inteiro positivo menor
	 * que o limite dado).
	 *
	 * @param reader     reader para concretizar a leitura
	 * @param playerName o nome do jogador
	 * @param maxBet     o limite maximo do numero a ler
	 * @return o numero lido
	 * @requires {@code reader != null && maxBet >=2}
	 * @ensures {@code \return >= 1 && \return <= maxBet}
	 */
	static int readValue(Scanner reader, String playerName, int maxBet) {
		System.out.print(playerName + ", quantos feijoes tens para jogar? (limite: " + maxBet + ") ");
		int totalMoney = readIntInInterval(1, maxBet, reader,
				"Erro: tem de ir a jogo com pelo menos 1 feijao e o limite sao " + maxBet + " feijoes. ");
		return totalMoney;
	}

	/**
	 * Le um valor inteiro no intervalo entre min e max. Se numero lido nao verifica
	 * a condicao repete a leitura, acompanhado de mensagem de erro. A leitura de
	 * coisas que nao sao numeros inteiros causa um erro durante a execucao.
	 *
	 * @param min      o limite minimo do numero a ler
	 * @param max      o limite maximo do numero a ler
	 * @param reader   reader para concretizar a leitura
	 * @param errorMsg mensagem a apresentar em caso de erro
	 * @return o numero lido
	 * @requires {@code reader != null && min < max && errorMsg != null }
	 * @ensures {@code \return >= min && \return <= max}
	 */
	static int readIntInInterval(int min, int max, Scanner channel, String errorMsg) {
		boolean error;
		int num;
		do {
			num = channel.nextInt();
			error = (num > max || num < min);
			if (error)
				System.out.print(errorMsg);
		} while (error);
		return num;
	}

}
