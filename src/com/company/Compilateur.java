package com.company;
import java.util.regex.*;
import java.util.HashMap;

public class Compilateur {
    public static void Commande(String ligne,HashMap<String, Double> symboles) throws CommandeInexistanteException, ExpressionFausseException, NomInvalideException, FonctionIntrouvableException, ParentheseFermanteException, VariableInexistanteException {


        String commande= ligne.contains(" ") ? ligne.split(" ")[0] : ligne ;

       if (commande.equals("let")) {
           String l[]=ligne.split(" ",4);

           if (l[2].equals("=")){
            Inserer_Var(l[1], Expression(l[3],symboles),symboles);
               System.out.println("OK");

           }
           else{
               throw new ExpressionFausseException("Erreur : Signe '=' manquant.");
           }
       }
       else if (commande.equals("print") ){
           String l[]=ligne.split(" ",2);
         System.out.println("la valeur est : "+Expression((l[1]),symboles));
       }
       else if (commande.equals("end") ){
           System.out.println("Fin du programme");
       }
        else
       { throw new CommandeInexistanteException("Erreu : la commande: "+ligne+" n'existe pas");}

    }
    public static double Expression(String expression, HashMap<String, Double> symboles) throws FonctionIntrouvableException, ExpressionFausseException, ParentheseFermanteException, VariableInexistanteException {
        return new Object() {
        int position = -1, chaine;
        void nextChar() {
            chaine = (++position < expression.length()) ? expression.charAt(position) : -1;
        }
        boolean eat(int charToEat) {
            while (chaine == ' ') nextChar();
            if (chaine == charToEat) {
                    nextChar();
                    return true;
            }
            return false;
            }
            double parse() throws ExpressionFausseException, FonctionIntrouvableException, ParentheseFermanteException, VariableInexistanteException {
                nextChar();
                double x = parseExpression();
                if (position < expression.length()) throw new ParentheseFermanteException("Erreur : Parenthèse(s) ouvrante(s) oubliée(s)");
                return x;
            }
            double parseExpression() throws FonctionIntrouvableException, VariableInexistanteException {
                double Res = parseTerm();
                for (;;) {
                    if      (eat('+')) Res += parseTerm(); // addition
                    else if (eat('-')) Res -= parseTerm(); // subtraction
                    else return Res;
                }
            }
            double parseTerm() throws FonctionIntrouvableException, VariableInexistanteException {
                double Res = parseFactor();
                for (;;) {
                    if      (eat('*')) Res *= parseFactor(); // multiplication
                    else if (eat('/')) Res /= parseFactor(); // division
                    else return Res;
                }
            }
            double parseFactor() throws FonctionIntrouvableException, VariableInexistanteException {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double Res;
                int startPos = this.position;
                if (eat('(')) { // parentheses
                    Res = parseExpression();
                    eat(')'); }
                 else if ((chaine >= '0' && chaine <= '9') || chaine == '.') { // numbers
                    while ((chaine >= '0' && chaine <= '9') || chaine == '.') nextChar();
                    Res = Double.parseDouble(expression.substring(startPos, this.position));
                } else if (Pattern.matches("[a-zA-Z$_]",Character.toString ((char) chaine)))  { nextChar();
                    // functions or variables
                    while (Pattern.matches("[a-zA-Z0-9$_]*",Character.toString ((char) chaine)))  nextChar() ;

                    String func = expression.substring(startPos, this.position);

                    if (symboles.containsKey(func)) {
                        Res = symboles.get(func);
                    }
                    else { double xx = parseFactor();
                    if (func.equals("sqrt")) Res = Math.sqrt(xx);
                    else if (func.equals("sin")) Res = Math.sin(Math.toRadians(xx));
                    else if (func.equals("cos")) Res = Math.cos(Math.toRadians(xx));
                    else if (func.equals("tan")) Res = Math.tan(Math.toRadians(xx));
                    else if (func.equals("abs")) Res = Math.abs(xx);
                    else if (func.equals("log")) Res = Math.log(xx);

                    else { throw new FonctionIntrouvableException("Erreur : Fonction " + func+" éronnée");};

                    }
                } else {

                    System.out.println(expression.substring(startPos-this.position, this.position));
                    throw new VariableInexistanteException("Erreur : cette expression est érronnée");
                }

                if (eat('^')) Res = Math.pow(Res, parseFactor()); // exponentiation

                return Res;
            }
        }.parse();
    }

    public static void Inserer_Var(String nom,Double valeur, HashMap<String, Double> symboles) throws NomInvalideException {
        if (nom.equals("let") || nom.equals("print") || nom.equals("end") || nom.equals("sin") || nom.contains("cos") || nom.contains("sqrt") || nom.contains("tan") || nom.contains("abs") || nom.contains("tan"))
            throw new NomInvalideException("Erreur : Le nom de variable ne peut pas être une commande/fonction standard");
        else {
            if (Pattern.matches("[a-zA-Z$_][a-zA-Z0-9$_]*", nom) == false) {
                throw new NomInvalideException("Erreur : Le nom de variable est invalide");
            } else {

                if (symboles.containsKey(nom)) symboles.replace(nom, valeur);
                else symboles.put(nom, valeur);


                }
            }
        }
    }



