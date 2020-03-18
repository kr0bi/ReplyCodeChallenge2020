import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    private static int W;
    private static int H;
    public static void main(String[] args) {
        String fileName = args[0];
        int NUM_TENTATIVI = Integer.parseInt(args[1]);

        File in = new File(fileName);
        try {
            Scanner scn = new Scanner(in);

            // leggi piano
            W = scn.nextInt();
            H = scn.nextInt();
            //System.out.println("W: "+W+" H: "+H);
            int nD = 0;
            int nM = 0;
            char[][] floor = new char[H][W];
            for(int i=0; i<H; i++) {
                String t = scn.next();
                for(int j=0; j<W; j++) {
                    floor[i][j] = t.charAt(j);
                    if (floor[i][j] == '_') {
                        nD++; }
                    if (floor[i][j] == 'M') {
                        nM++;
                    }
                    //System.out.print(floor[i][j]);
                }
                //System.out.print("\n");
            }
            //System.out.println("_: "+nD+"  M: "+nM);

            // leggi developers
            int D_n = scn.nextInt();
            Developer[] D = new Developer[D_n];
            //System.out.println("D_n: "+D_n);
            for(int i=0; i<D_n; i++) {
                String C = scn.next();
                //System.out.println("C: "+C);
                int B = scn.nextInt();
                //System.out.println("B: "+B);
                int S_n = scn.nextInt();
                //System.out.println("S_n: "+S_n);
                Skills S = new Skills();
                for(int j=0; j<S_n; j++) {
                    String skill = scn.next();
                    S.addSkill(skill);
                    //System.out.print(skill+" ");
                }
                S.sortSkill();
                //System.out.print("\n");
                D[i] = new Developer( C, B, S, i);
            }
            /*for(int i=0; i<D_n; i++) {
                System.out.println(D[i].getTheCompany());
            }*/

            // leggi managers
            int M_n = scn.nextInt();
            ProjectManager[] M = new ProjectManager[M_n];
            //System.out.println("M_n: "+M_n);
            for(int i=0; i<M_n; i++) {
                String C = scn.next();
                //System.out.println("C: "+C);
                int B = scn.nextInt();
                //System.out.println("B: "+B);
                M[i] = new ProjectManager(C, B, i);
            }

            scn.close();

            // qui poi lavorare

            // soluzione veloce
            // <---

            // istanzio matrice floor migliore
            Impiegato[][] best = new Impiegato[H][W];
            for(int i=0; i<H; i++) {
                for(int j=0; j<W; j++) {
                    best[i][j] = null;
                }
            }
            //best[0][0] = D[0];
            //System.out.println(best[0][0].getTheCompany());

            // istanzio matrice floor soluzione attuale
            Impiegato[][] act = new Impiegato[H][W];
            for(int i=0; i<H; i++) {
                for(int j=0; j<W; j++) {
                    act[i][j] = null;
                }
            }

            /*for(int d=0; d<nD; d++) {
                for(int i=0; i<D_n; i++) {

                }
            }*/

            // provo ad inserirli a caso e vedere che mi da
            /*int t1 = 0;
            int t2 = 0;
            int best_score = -1;
            for(int i=0; i<H; i++) {
                for(int j=0; j<W; j++) {
                    if(floor[i][j] == '_') {
                        best[i][j] = D[t1];
                        t1++;
                    }
                    if(floor[i][j] == 'M') {
                        best[i][j] = M[t2];
                        t2++;
                    }
                }
            }*/

            int best_score = -1;
            for(int tent=0; tent<NUM_TENTATIVI; tent++) {
                // resetto matrice act
                for(int i=0; i<H; i++) {
                    for(int j=0; j<W; j++) {
                        act[i][j] = null;
                    }
                }
                // calcolo ACT mettendo i primi
                int t1 = 0;
                int t2 = 0;
                for(int i=0; i<H; i++) {
                    for(int j=0; j<W; j++) {
                        if(floor[i][j] == '_' && t1 < D_n) {
                            act[i][j] = D[t1];
                            t1++;
                        }
                        if(floor[i][j] == 'M' && t2<M_n) {
                            act[i][j] = M[t2];
                            t2++;
                        }
                    }
                }
                // calcolo score
                int act_score = 0;
                for(int i=0; i<H; i++) {
                    for(int j=0; j<W; j++) {
                        // GIU
                        if (i+1 < H) {
                            Impiegato imp1 = act[i][j];
                            Impiegato imp2 = act[i+1][j];
                            Score sc1 = new Score(imp1, imp2);
                            act_score += sc1.getTotalPotential();
                        }
                        // DESTRA
                        if (j+1<W) {
                            Impiegato imp = act[i][j];
                            Impiegato imp2 = act[i][j+1];
                            Score sc1 = new Score(imp, imp2);
                            act_score+= sc1.getTotalPotential();
                        }
                    }
                }
                if(act_score > best_score) {
                    // copio act -> best
                    for(int i=0; i<H; i++) {
                        Impiegato[] row = act[i];
                        int row_l = row.length;
                        best[i] = new Impiegato[row.length]; // W
                        System.arraycopy(row, 0, best[i], 0, row_l);
                    }
                    best_score = act_score;
                }

                // shufflo array; praticamente sto provando soluzioni random
                shuffleArrayDev(D);
                shuffleArrayMan(M);
            }

            // soluzione ottimale?
            // tengo lista impiegati nessuno utilizzato
            // for numero developer da disporre
            // for numero developer (attenzione togliere quelli già usati)
            // ne scelgo uno
            // for numero manager da disporre
            // for numero manager (attenzione togliere quelli già usati)
            // ne scelgo uno
            // calcolo score
            // continuo, se migliore sostituisco la lista attuale a quella migliore


            // ora ho la matrice BEST
            // trovo la lista degli impiegati nell'ordine in cui sono disposti
            Coord[] pos_dev = new Coord[D_n];
            Coord[] pos_man = new Coord[M_n];
            for(int i=0; i<D_n; i++) {
                pos_dev[i] = new Coord();
            }
            for(int i=0; i<M_n; i++) {
                pos_man[i] = new Coord();
            }
            //System.out.println(pos_dev[0].x+" "+pos_dev[0].y);
            for(int i=0; i<H; i++) {
                for(int j=0; j<W; j++) {
                    if(best[i][j] != null) {
                        if(best[i][j] instanceof Developer) {
                            pos_dev[((Developer) best[i][j]).getId()] = new Coord(i,j);
                        }
                        if(best[i][j] instanceof ProjectManager) {
                            pos_man[((ProjectManager) best[i][j]).getId()] = new Coord(i,j);
                        }
                    }
                }
            }

            System.out.println(best_score);

            // output
            try {
                PrintWriter out = new PrintWriter("out.txt", "UTF-8");
                // stampo developer
                for(int i=0; i<D_n; i++) {
                    if(pos_dev[i].x == -1) {
                        System.out.println("X");
                    }
                    else {
                        System.out.println(pos_dev[i].x+" "+pos_dev[i].y);
                    }
                }
                // stampo manager
                for(int i=0; i<M_n; i++) {
                    if(pos_man[i].x == -1) {
                        System.out.println("X");
                    }
                    else {
                        System.out.println(pos_man[i].x+" "+pos_man[i].y);
                    }
                }
                out.close();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace(); }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(); }
    }

    static void shuffleArrayDev(Developer[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        //Random rnd = ThreadLocalRandom.current();
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Developer a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    static void shuffleArrayMan(ProjectManager[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        //Random rnd = ThreadLocalRandom.current();
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            ProjectManager a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}

class Coord {
    public int x=-1;
    public int y=-1;

    public Coord() {}

    public Coord(int a, int b) {
        x=b;
        y=a;
    }
}

class Score{

    private int workPotential;
    private int bonusPotential;

    private List<Impiegato> coppiaImp;  //lista di 2 elementi

    public Score(int wp, int  bp, List<Impiegato> cd){
        workPotential = wp;
        bonusPotential = bp;
        coppiaImp = cd;
    }

    public Score(List<Impiegato> cd){
        workPotential = 0;
        bonusPotential = 0;
        coppiaImp = cd;
    }

    public Score(Impiegato a, Impiegato b){
        workPotential = 0;
        bonusPotential = 0;
        coppiaImp = new ArrayList<Impiegato>();
        addDevelopers(a, b);
    }

    public Score(){
        workPotential = 0;
        bonusPotential = 0;
        coppiaImp = new ArrayList<Impiegato>();
    }


    public int getWorkPotential(){
        return workPotential;
    }
    public int getBonusPotential(){
        return bonusPotential;
    }

    //QUESTO METODO MODIFICA WORKPOTENTIAL
    public int setWorkPotential (){

        Impiegato firstDev = coppiaImp.get(0);
        Impiegato secondDev = coppiaImp.get(1);

        //calcolo l'intersezione delle skill in comune
        try {
            int numberOfSkillsInComune = firstDev.getSkills().compareTo(secondDev);

            //calcolo l'elenco di skills distinte
            int numberOfSkillsDistinte= firstDev.getSkills().distictTo(secondDev);

            int wp = numberOfSkillsInComune * numberOfSkillsDistinte;
            workPotential = wp;

            return this.workPotential;
        } catch (NullPointerException npe){
            return 0;
        }

    }

    //QUESTO METODO MODIFICA BONUSPOTENTIAL
    public int setBonusPotential(){
        Impiegato firstDev = coppiaImp.get(0);
        Impiegato secondDev = coppiaImp.get(1);

        int bp = 0;
        if (firstDev.getTheCompany().equalsIgnoreCase(secondDev.getTheCompany())){
            bp = firstDev.getTheBonus() * secondDev.getTheBonus();
        }
        bonusPotential = bp;

        return bonusPotential;
    }

    public int getTotalPotential (){
        try {
            setWorkPotential();
            setBonusPotential();
            return workPotential + bonusPotential;
        } catch (NullPointerException npe) {
            return 0;
        }
    }


    public void addDevelopers(Impiegato A, Impiegato B){
        coppiaImp.add(A);
        coppiaImp.add(B);
    }


}

interface Impiegato{
    public int getId();
    public String getTheCompany();
    public int getTheBonus();
    public Skills getSkills();
    public void setId(int i);
    public void setTheCompany(String c);
    public void setTheBonus(int b);
    public void setSkills(Skills s);

}

abstract class ImpiegatoI implements Impiegato {
    private int id;
    private String theCompany;
    private int theBonus;
    private Skills theSkills;

    public ImpiegatoI(){
        id = 0;
        theCompany = "";
        theBonus = 0;
        theSkills = new Skills();
    }

    public ImpiegatoI (int i, String c, int b, Skills s){
        id = i;
        theCompany = c;
        theBonus = b;
        theSkills = s;
    }

    public int getId(){
        return id;
    }
    public String getTheCompany(){
        return theCompany;
    }
    public int getTheBonus(){
        return theBonus;
    }
    public Skills getSkills(){
        return theSkills;
    }

    public String stampaSkills(){
        return theSkills.toString();
    }

    public void setId(int i){
        id = i;
    }
    public void setTheCompany(String c){
        theCompany = c;
    }
    public void setTheBonus(int b){
        theBonus = b;
    }
    public void setSkills(Skills s){
        theSkills = s;
    }
}


class Developer extends ImpiegatoI{

    public Developer(String c, int b, Skills s, int i){
        super.setTheCompany(c);
        super.setTheBonus(b);
        super.setSkills(s);
        super.setId(i);
    }

}

class ProjectManager extends ImpiegatoI{

    public ProjectManager(String c, int b, int npm){
        super.setId(npm);
        super.setTheCompany(c);
        super.setTheBonus(b);
        super.setSkills(new Skills());
    }

}

class Skills {
    private List<String> elencoSkill;

    public Skills(List<String> es){
        elencoSkill = es;
    }
    //
    public Skills(){
        elencoSkill = new ArrayList<String>();
    }

    private Skills(String nuovaSkill){
        elencoSkill.add(nuovaSkill);
    }

    public void addSkill (String nuovaSkill){
        elencoSkill.add(nuovaSkill);
    }


    public List<String> getElencoSkills(){
        return elencoSkill;
    }

    //calcolo il numero di skills distinte
    public int distictTo(Impiegato B){

        List<String> bSkills = B.getSkills().getElencoSkills();
        List<String> aSkills = this.getElencoSkills();
        int aSkillsLength = aSkills.size();
        int bSkillsLength = bSkills.size();
        int commonSkills = this.compareTo(B);

        return aSkillsLength + bSkillsLength - 2*commonSkills;

    }
    public void sortSkill(){
        java.util.Collections.sort(elencoSkill);
    }

    /**
     *
     * @param B
     * @return il numero di skills in comune
     */

    public int compareTo(Impiegato B){
        int commonSkills = 0;
        Skills bSkills = B.getSkills();
        int i = 0;
        int j = 0;

        while(i<elencoSkill.size() && j < bSkills.getElencoSkills().size()){
            if(elencoSkill.get(i).compareTo(bSkills.getElencoSkills().get(j)) == 0){
                commonSkills++;
                i++;
                j++;
            } else {
                if(elencoSkill.get(i).compareTo(bSkills.getElencoSkills().get(j))<0){
                    i++;
                } else {
                    j++;
                }
            }
        }
        return commonSkills;
    }

    public String toString(){
        Iterator<String> it = getElencoSkills().iterator();
        String str = "";
        while (it.hasNext()){
            str = str + it.next() +  " ";
        }
        return str;
    }

}