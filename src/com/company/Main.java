package com.company;

        ;
import java.util.HashMap;
import java.util.Scanner;

import static com.company.Compilateur.Commande;
import static com.company.Compilateur.Expression;

public class Main {

    public static void main(String[] args) throws CommandeInexistanteException, ExpressionFausseException, NomInvalideException, FonctionIntrouvableException, ParentheseFermanteException {
        HashMap<String,Double> Symboles = new HashMap<String,Double>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez vos commandes. Tapez end pour terminer votre programme.\n");
        System.out.print(" Une commande doit être de la forme \n");
        System.out.print("let <variable> = <expression> \n");
        System.out.print("ou\n");
        System.out.print("print <expression>\n");
        System.out.print("> ");
        String l = sc.nextLine();
        while ( l.equals("end") == false) {
            try {

                Commande(l, Symboles);


            } catch (ParentheseFermanteException e) {
                System.out.println(e.getMessage());

            } catch (ExpressionFausseException e) {
                System.out.println(e.getMessage());
            } catch (CommandeInexistanteException e) {
                System.out.println(e.getMessage());
            }
            catch (NomInvalideException e) {
                System.out.println(e.getMessage());
            } catch (VariableInexistanteException e) {
                System.out.println(e.getMessage());

            } catch (FonctionIntrouvableException e) {
                System.out.println(e.getMessage());

            }
            finally {
                System.out.print("> ");
                l = sc.nextLine();
            }
        }
        System.out.println("Fin du programme.");

    }
}
