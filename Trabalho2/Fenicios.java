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

    int nodo_anterior = 1;
    int nodo_proximo = 0;

    boolean porto_alcancado = false;

    boolean visitado[][]; //marca se foi visitado ou n na pos

    int dl[] = { -1, 1, 0, 0 }; //vetores da posição norte, sul, leste, oeste para linhas e colunas
    int dc[] = { 0, 0, 1, -1 };

    public void lerMapa(){
        
        try {
            int numero;
            File file = new File("teste1.txt");
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

    public int caminho(int origem, int destino){ 
        int movimentos = 0;
        //"resseta" tudo para seu valor original
        visitado = new boolean[nLinhas][nColunas]; 
        lq = new ArrayDeque<Integer>();
        cq = new ArrayDeque<Integer>();
        porto_alcancado = false;
        nodo_anterior = 1;
        nodo_proximo = 0;

        lq.add(linha_origem[origem-1]);
        cq.add(coluna_origem[origem -1]);
        visitado[linha_origem[origem-1]][coluna_origem[origem-1]] = true;

        while(lq.size() > 0){
            int l = lq.remove();
            int c = cq.remove();
            if(matriz[l][c].equals(Integer.toString(destino))){
                porto_alcancado = true;
                break;
            }
            explorar_vizinhos(l, c);
            nodo_anterior--;
            if(nodo_anterior == 0){
                nodo_anterior = nodo_proximo;
                nodo_proximo = 0;
                movimentos++;
            }
        }
        if(porto_alcancado){
            return movimentos; 
        }
        return -1;
    }

    public int total() {
        int total = 0;
        int origem = 1;
        int destino = 2;
        
        while (origem <9 && destino < 10) {
            int subtotal = caminho(origem, destino);

            if (subtotal != -1){ //se o porto foi alcançado
                total = total + subtotal;
                origem = destino; //porto de origem vira onde chegou, pode pular os n alcançáveis
                destino++;
            }
            else {
                destino++; 
            }
        }

        total = total + caminho(origem, 1); //volta para casa

        return total;
    }

    public static void main(String[]args){
        Fenicios f = new Fenicios();
        f.lerMapa();
        System.out.println(f.total());
    }
}
