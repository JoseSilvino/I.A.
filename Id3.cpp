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
Node* createTree(vector < vector < string > > &base){
    int vec_vec_size,vec_size;
    cout<<"Tell how many data the base has\n";
    cin>>vec_vec_size;
    cout<<"Tell how many attributes the base has\n";
    cin>>vec_size;
    base = vector<vector <string> >(vec_vec_size,vector<string>(vec_size,""));
    cout<<"Write base attributes\n";
    or_atts.resize(vec_size);
    for(int i=0;i<vec_size;i++) cin>>or_atts[i];
    cout<<"Write users' atributes in the same order as before\n";
    for(int i =0;i<vec_vec_size;i++) for(int j=0;j<vec_size;j++) {cin>>base[i][j];cout<<i<<" "<<j<<endl;}
    Node* n = new Node();
    vector<int> vi;
    for(int i=0;i<(int)base.size();i++) vi.push_back(i);
    auto n_or_atts = removefrom(or_atts,or_atts[vec_size-1]);
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
int main(){
    Node* n;
    //substituir por receber a base de dados
    vector < vector < string > > base,Evaluationbase,Trainbase;
    while(1){
        cout<<"Choose\n1. Training\n2. Evaluation\nOther. Exit\n";
        int d;
        cin>>d;
        if(d==1){
            n = createTree(Trainbase);
        }else if(d==2){
            Evaluationbase = Complement(Trainbase,base);
            cout<<"Write users' attributes in the right order\n";
            vector<string> classify(or_atts.size(),"");
            int i;
            for(i =0;i<(int)(or_atts.size()-1);i++)cin>>classify[i];
            string last;
            cin>>last;
            string classificated;
            Go_Classify(n,classify,classificated);
            classify.push_back(last);
            if(last!=classificated)cout<<"The Value of"<<or_atts[or_atts.size()-1]<<" is different from the tree's result\n";
            else cout<<"The value of"<<or_atts[or_atts.size()-1]<<" is equal from the tree's result\n";
            base.push_back(classify);
        }else break;
        auto matrix = Generate_Confusion_Matrix(n,base);
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
    }
    return 0;
}
