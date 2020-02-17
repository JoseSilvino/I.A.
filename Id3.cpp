#include <bits/stdc++.h>
using namespace std;
vector < string >  or_atts;
ostream& operator<<(ostream& o,vector<string> v){
    for(auto x:v)
        o<<x<<" ";
    return o;
}
template <typename t>
bool contains(vector<t> vet,t c){
    for(t x : vet) if(x==c) return true;
    return false;
}
int indexof(vector<string> v,string s){
    for(int i=0;i<(int)v.size();i++) if(v[i]==s) return i;
    return -1;
}
vector<string> system_classes (vector < vector < string > > base){
    vector<string> classes;
    for(vector<string> x:base){
        if(!contains(classes,x[x.size()-1])){
            classes.push_back(x[x.size()-1]);
        }
    }
    return classes;
}
int occurrences(string val,int index,vector < vector < string > > base){
    int oc=0;
    for(auto x:base){
        if(x[index]==val) oc++;
    }
    return oc;
}
double entropy_system(vector < vector < string > > base){
    vector<string> sc = system_classes(base);
    double result=0;
    for(int i = 0 ; i < (int)sc.size();i++){
        double divi = (double)occurrences(sc[i],(base[0].size())-1,base)/(double)base.size();
        result += (divi)*log2(divi);
    }
    return -result;
}
double entropy(string att,vector<int> regs,vector < vector < string > > base){
    int j = indexof(or_atts,att);
    vector<string> values;
    vector<int> freq(regs.size(),0);
    for(int x:regs){
        if(!contains(values,base[x][j])){
            values.push_back(base[x][j]);
        }
        freq[indexof(values,base[x][j])]++;
    }
    vector <string> sc = system_classes(base);
    vector <vector <int> > freqvalues(sc.size(),vector<int>(values.size(),0));
    for(int i : regs){
        for(int k = 0 ;k < (int)values.size();k++){
            if(values[k]==base[i][j]){
                int ind = indexof(sc,base[i][(base[i].size())-1]);
                freqvalues[ind][k]++;
            }
        }
    }
    double sum = 0.0;
    for(int k=0;k<(int)values.size();k++){
        double partialsum = 0.0;
        for(int i =0;i<(int)sc.size();i++){
            if(freqvalues[i][k]!=0){
                double d = (double)((double)freqvalues[i][k]/(double)freq[k]);
                partialsum-= (d)*log2(d);
            }
        }
        sum+= (double)((double)freq[k]/(double)regs.size())*partialsum;
    }
    return sum;
}
double gain(vector<int>regs,string att,vector < vector < string > > base){
    return entropy_system(base)-entropy(att,regs,base);
}
string max_gain(vector<int> regs,vector<string> atts,vector < vector < string > > base){
    string att;
    int maxgain = INT_MIN;
    for(string x : atts){
        if(gain(regs,x,base) > maxgain){
            maxgain = gain(regs,x,base);
            att = x;
        }
    }
    return att;
}

int max_value(vector<int> v){
    int max_v = 0;
    for(int i=1;i<(int)v.size();i++){
        if(v[i]>v[max_v]) max_v = i;
    }
    return max_v;
}
pair<vector<string>,int> get_classes(vector<int> regs,vector < vector < string > > base){
    vector<string> ret;
    vector<int> freq(regs.size(),0);
    for(int i:regs){
        string classe = base[i][((int)base[i].size())-1];
        if(!contains(ret,classe)){
            ret.push_back(classe);
        }
        freq[indexof(ret,classe)]++;
    }
    return make_pair(ret,max_value(freq));
}
string predominant_class(vector<int> regs,vector < vector < string > > base){
    pair<vector<string>,int> x = get_classes(regs,base);
    return x.first[x.second];
}
double errorrt(string pc,vector<int>regs,vector < vector < string > > base){
    double err =0.0;
    for(int i:regs){
        if(base[i][base[i].size()-1]!=pc) err++;
    }
    return err/(double)regs.size();
}
vector<string> removefrom(vector<string> v,string s){
        vector<string> n_v;
        for(auto str:v) if(str!=s) n_v.push_back(str);
        return n_v;
    }
vector<int> remove_not_equals(vector<int> v,int index,string value,vector < vector < string > > base){
        vector<int> n_v;
        for(auto i:v) if(base[i][index]==value) n_v.push_back(i);
        return n_v;
    }
class Node{
    public:
    vector<Node*> children;
    string classification;
    string attribute,value;
    double error_rt;
    Node(){
       this->children.resize(0);
       this->attribute = "First Node";
    }
    Node(string classification,double error_rt){
        this->classification = classification;
        this->error_rt = error_rt;
        this->attribute="";
        this->value="";
    }
    Node(string value,string attribute){
        this->value = value;
        this->attribute = attribute;
        this->classification = "";
        this->error_rt=0.0;
    }

    void generate_node(vector<int> registers,vector<string> attributes,vector < vector < string > > base){
        vector<string> classes = get_classes(registers,base).first;
        if(classes.size()>1){
            if(attributes.size()>0){
                string attribute = max_gain(registers,attributes,base);
                int index = indexof(or_atts,attribute);
                vector<string> values;
                for(int x : registers){
                    if(!contains(values,base[x][index])) values.push_back(base[x][index]);
                }
                for(auto value:values){
                    auto n = new Node(value,attribute);
                    children.push_back(n);
                    auto n_attributes = removefrom(attributes,attribute);
                    auto n_regs = remove_not_equals(registers,index,value,base);
                    n->generate_node(n_regs,n_attributes,base);
                }
            }else{
                string pc = predominant_class(registers,base);
                double rt = errorrt(pc,registers,base);
                this->classification = pc;
                this->error_rt = rt;
            }
        }else{
            this->classification=classes[0];
            this->error_rt=0.0;
        }
    }
    bool isLeaf(){
        return this->children.size()==0;
    }
};
void print_pre(Node* n){
    if(n!=NULL){
        cout<<"Attribute : "<<n->attribute<<"| Value : "<<n->value<<" |Error : "<<n->error_rt<<" |Class : "<<n->classification<<endl;
        for(auto c:n->children) print_pre(c);
    }
}
void Go_Classify(Node* n,vector<string> to_classify,string& ret){
    if(n->isLeaf()) {
        cout<<"Classificated as "<<n->classification<<" in "<<or_atts[or_atts.size()-1]<<endl;
        ret = n->classification;
        }
    else{
        for(auto child:n->children){
            if(to_classify[indexof(or_atts,child->attribute)]==child->value){
                Go_Classify(child,to_classify,ret);
                break;
            }
        }
    }
}
Node* createTree(vector < vector < string > > base){
    Node* n = new Node();
    vector<int> vi;
    for(int i=0;i<(int)base.size();i++) vi.push_back(i);
    auto n_or_atts = removefrom(or_atts,or_atts[or_atts.size()-1]);
    n->generate_node(vi,n_or_atts,base);
    cout<<"Pre order print of the decision tree\n";
    print_pre(n);
    return n;
}
vector< vector<int> > Generate_Confusion_Matrix(Node* n,vector<vector <string> >EvaluationBase){
    vector<string> sc = system_classes(EvaluationBase);
    int scsize = sc.size();
    vector<vector <int> > matrix(scsize,vector<int>(scsize,0));
    for(int i=0;i<scsize;i++){
        for(int j=0;j<(int)EvaluationBase.size();j++){
            string real = EvaluationBase[j][or_atts.size()-1];
            string in_tree;
            auto tmp = removefrom(EvaluationBase[j],EvaluationBase[j][or_atts.size()-1]);
            Go_Classify(n,tmp,in_tree);
            int ind1,ind2;
            ind1 = indexof(sc,real);
            ind2 = indexof(sc,in_tree);
            matrix[ind1][ind2]++;
        }
    }
    return matrix;
}
double recall(vector <vector <int> >matrix,int i){
    int size = matrix.size();
    double rec = 0.0;
    for(int j =0;j<size;j++) rec+=matrix[i][j];
    return (double)((double)matrix[i][i]/rec);
}
double precision(vector <vector <int> >matrix,int j){
    int size = matrix.size();
    double rec = 0.0;
    for(int i=0;i<size;i++) rec+=matrix[i][j];
    return (double)((double)matrix[j][j]/rec);
}
double accuracy(vector<vector <int> >m){
    int size=m.size();
    double diagonal = 0.0,total = 0.0;
    for(int i=0;i<size;i++) diagonal+=m[i][i];
    for(int i=0;i<size;i++) for(int j=0;j<size;j++) total+=m[i][j];
    return diagonal/total;
}
vector< vector<string> > generate_db(){
    cout<<"Tell the number of elements in the database\n";
    int vec_vec_sz,vec_sz;
    cin>>vec_vec_sz;
    cout<<"Tell the number of attributes of the database\n";
    cin>>vec_sz;
    vector<vector<string> > base(vec_vec_sz,vector<string>(vec_sz,""));
    cout<<"Tell the attributes' names\n";
    or_atts.reserve(vec_sz);
    for(int i=0;i<vec_sz;i++)cin>>or_atts[i];
    cout<<"Tell the"<<vec_vec_sz<<" users and the "<<vec_sz<<"attributes of them in the same order\n";
    for(int i=0;i<vec_vec_sz;i++)for(int j=0;j<vec_sz;j++) cin>>base[i][j];
    return base;
}
vector < vector<string> > RandomPartition(vector < vector<string> >base){
    srand(time(NULL));
    size_t number_of = 0.8*base.size();
    vector<int> sorted;
    while(sorted.size()<number_of){
        int n = rand()%base.size();
        while(contains(sorted,n)) n = rand()%base.size();
        sorted.push_back(n);
    }
    vector< vector<string> > ret;
    for(auto x:sorted) ret.push_back(base[x]);
    return ret;
}
bool equals(vector<string> v1,vector<string>v2){
    if(v1.size()!=v2.size()) return false;
    for(size_t i=0;i<v1.size();i++) if(v1[i]!=v2[i]) return false;
    return true;
}
bool contains(vector< vector<string> > vvs,vector<string> vs){
    for(auto x:vvs)  if(equals(x,vs)) return true;
    return false;
}
vector < vector<string> > Complement(vector< vector<string> > or_base,vector< vector<string> > train_base){
    vector< vector<string> >evaluationbase;
    for(auto x:or_base) if(!contains(train_base,x)) evaluationbase.push_back(x);
    return evaluationbase;
}
int main(){
    Node* n;
    //substituir por receber a base de dados
    vector < vector < string > > base,Evaluationbase,Trainbase;
    base = generate_db();
    while(1){
        cout<<"Choose\n1. Training\n2. Evaluation\nOther. Exit\n";
        int d;
        cin>>d;
        if(d==1){
            Trainbase = RandomPartition(base);
            n = createTree(Trainbase);
        }else if(d==2){
            Evaluationbase = Complement(Trainbase,base);
            auto matrix = Generate_Confusion_Matrix(n,Evaluationbase);
            cout<<"Confusion matrix\n";
            for(auto x:matrix) {
                for (auto y:x) cout<<y<<" ";
                cout<<endl;
            }
            double acc = accuracy(matrix);
            cout<<"Accuracy : "<<acc<<endl;
            cout<<"Error Rate : "<<1-acc<<endl;
            auto sc = system_classes(base);
            for(int i =0;i<(int)matrix.size();i++){
                cout<<"Recall ("<<sc[i]<<") : "<<recall(matrix,i)<<endl;
                cout<<"Precision ("<<sc[i]<<") : "<<precision(matrix,i)<<endl;
            }
        }else break;
        
    }
    return 0;
}
