import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Fenicios{
        
    int nLinhas = 0;
    int nColunas = 0;
    String matriz[][];
    int linha_origem[] = new int[9]; 
    int coluna_origem[] = new int[9];
    Queue<Integer> lq = new ArrayDeque<Integer>();
    Queue<Integer> cq = new ArrayDeque<Integer>();

    int move_count = 0;
    int nodo_anterior = 1;
    int nodo_proximo = 0;

    boolean porto_alcancado = false;

    boolean visitado[][];

    int dl[] = { -1, 1, 0, 0 };
    int dc[] = { 0, 0, 1, -1 };

    public void lerMapa(){
        
        try {
            int numero;
            File file = new File("case0.map");
            Scanner scanner = new Scanner(file);
            nLinhas = scanner.nextInt();
            nColunas = scanner.nextInt();
            scanner.nextLine();
            matriz = new String[nLinhas][nColunas];
            visitado = new boolean[nLinhas][nColunas];
            ArrayList<int[]> posicoes = new ArrayList<>();
            for (int lin = 0; lin < nLinhas; lin++) {
                String valores[] = scanner.nextLine().split("");
                for (int col = 0; col < nColunas; col++) {
                    matriz[lin][col] = valores[col];
                    if (matriz[lin][col].matches("\\d+")) {
                        numero = Integer.parseInt(matriz[lin][col]);
                        int [] posicao = {numero, lin, col};
                        posicoes.add(posicao);
                    }
                }
            }
            for (int i = 0; i < posicoes.size() - 1; i++) {
                for (int j = 0; j < posicoes.size() - i - 1; j++) {
                    if (posicoes.get(j)[0] > posicoes.get(j + 1)[0]) {
                        int[] temp = posicoes.get(j);
                        posicoes.set(j, posicoes.get(j + 1));
                        posicoes.set(j + 1, temp);
                    }
                }
            }
            for (int i = 0; i < posicoes.size(); i++) {
                int[] posicao = posicoes.get(i);
                linha_origem[i] = posicao[1];
                coluna_origem[i] = posicao[2];
            }
            scanner.close();
         
        } catch (FileNotFoundException e) {
            System.err.format("Erro na leitura do arquivo: %s%n", e);
        }
        System.out.println(caminho());
    }

    public void explorar_vizinhos(int l, int c){
        for(int i=0; i<4; i++){
            int ll = l + dl[i];
            int cc = c + dc[i];

            if(ll<0 || cc<0){continue;}
            if(ll>=nLinhas || cc>=nColunas){continue;}

            if(visitado[ll][cc]){continue;}
            if(matriz[ll][cc].equals("*")){continue;}

            lq.add(ll);
            cq.add(cc);
            visitado[ll][cc] = true;
            nodo_proximo++;            
        }
    }

    public int caminho(){ //TESTE: porto 2 ao porto 3
        lq.add(linha_origem[1]);
        cq.add(coluna_origem[1]);
        visitado[linha_origem[1]][coluna_origem[1]] = true;
        while(lq.size() > 0){
            int l = lq.remove();
            int c = cq.remove();
            if(matriz[l][c].equals("3")){
                porto_alcancado = true;
                break;
            }
            explorar_vizinhos(l, c);
            nodo_anterior--;
            if(nodo_anterior == 0){
                nodo_anterior = nodo_proximo;
                nodo_proximo = 0;
                move_count++;
            }
        }
        if(porto_alcancado){
            return move_count; // RESP: 39
        }
        return -1;
    }

    public static void main(String[]args){
        Fenicios f = new Fenicios();
        f.lerMapa();
    }
}

/* print do mapa
for (int lin = 1; lin < nLinhas; lin++) {
    for (int col = 0; col < nColunas; col++) {
        System.out.print(matriz[lin][col] + " ");
    }
    System.out.println();
}
 */
