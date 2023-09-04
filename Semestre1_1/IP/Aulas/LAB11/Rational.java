/**********************************************************************************
 * Classe cujos objectos sao numeros racionais representados como fracoes na
 * forma irredutivel. Estes objetos são imutáveis.
 *
 * @author IP1920
 **********************************************************************************/

public class Rational {

    // atributos dos objetos Rational

    // o numerador e o denominador
    private final int num;
    private final int den;

    // invariante:
    // apos a execucao de qualquer construtor ou metodo sobre um Rational temos que
    // 1) den > 0
    // 2) den e num nao tem divisores comuns (sao primos entre si)

    /**
     * Constroi um Rational que representa um inteiro
     *
     * @param n o numero inteiro
     */
    public Rational(int n) {
        this.num = n;
        this.den = 1;
    }

    /**
     * Constroi um Rational que representa o quociente entre os dois numeros dados
     *
     * @param numerator O numerador do racional a construir
     * @param denominator O denominador do racional a construir
     * @requires {@code denominator != 0}
     */
    public Rational(int numerator, int denominator) {

    }

    /**
     * @return O numerador deste racional
     */
    public int getNumerator() {
        return num;
    }

    /**
     * @return O denominador deste racional
     */
    public int getDenonimator() {
        return den;
    }

    /**
     * Soma racionais
     *
     * @param other O outro racional
     * @return A soma deste racional com other.
     */
    public Rational add(Rational other) {
        int numerator = (this.num * other.den) + (this.den * other.num);
        int denominator = this.den * other.den;
        return new Rational(numerator, denominator);
    }

    /**
     * Multiplica racionais
     *
     * @param other O outro racional
     * @return O produto deste racional com other.
     */
    public Rational multiply(Rational other) {
        return new Rational(this.num * other.num, this.den * other.den);
    }

    /**
     * Divide racionais
     *
     * @param other O outro racional
     * @return O quociente deste racional pelo other.
     * @requires {@code other.getNumerator() != 0}
     */
    public Rational divide(Rational other) {
        // TODO
        return null; // CHANGE ME!
    }

    /**
     * Compara racionais.
     *
     * @param other O outro racional}
     * @return Se este racional e other representam o mesmo racional.
     */
    public boolean equalsRational(Rational other) {
        return num == other.num && dem = other.dem;
    }

    /**
     * Retorna uma representacao textual do racional.
     *
     * @return A representacao textual do objeto
     */
    public String toString() {
        if (den == 1)
            return num + "";
        else
            return num + "/" + den;
    }

}
