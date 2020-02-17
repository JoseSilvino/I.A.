#include <bits/stdc++.h>
using namespace std;
vector < vector < string > > base;
vector < string >  or_atts;
template <typename t>
bool contains(vector<t> vet,t c){
    for(t x : vet) if(x==c) return true;
    return false;
}
int indexof(vector<string> v,string s){
    for(int i=0;i<(int)v.size();i++) if(v[i]==s) return i;
    return -1;
}
vector<string> system_classes (){
    vector<string> classes;
    for(vector<string> x:base){
        if(!contains(classes,x[x.size()-1])){
            classes.push_back(x[x.size()-1]);
        }
    }
    return classes;
}
int occurrences(string val,int index){
    int oc=0;
    for(auto x:base){
        if(x[index]==val) oc++;
    }
    return oc;
}
double entropy_system(){
    vector<string> sc = system_classes();
    double result=0;
    for(int i = 0 ; i < (int)sc.size();i++){
        double divi = (double)occurrences(sc[i],base.size()-1)/(double)base.size();
        result += (divi)*log2(divi);
    }
    return -result;
}
double entropy(string att,vector<int> regs){
    int j = indexof(or_atts,att);
    vector<string> values;
    vector<int> freq(regs.size(),0);
    for(int x:regs){
        if(!contains(values,base[x][j])){
            values.push_back(base[x][j]);
        }
        freq[indexof(values,base[x][j])]++;
    }
    vector <string> sc = system_classes();
    vector <vector <int> > freqvalues(sc.size(),vector<int>(values.size(),0));
    for(int i : regs){
        for(int k = 0 ;k < (int)values.size();k++){
            int ind = indexof(sc,base[i][base.size()-1]);
            freqvalues[ind][k]++;
        }
    }
    double sum = 0.0;
    for(int k=0;k<(int)values.size();k++){
        double partialsum = 0.0;
        for(int i =0;i<(int)sc.size();i++){
            partialsum-= ((double)((double)freqvalues[i][k]/(double)freq[k]))*log2((double)((double)freqvalues[i][k]/(double)freq[k]));
        }
        sum+= (double)((double)freq[k]/(double)regs.size())*partialsum;
    }
    return sum;
}
double gain(vector<int>regs,string att){
    return entropy_system()-entropy(att,regs);
}
string max_gain(vector<int> regs,vector<string> atts){
    string att;
    int maxgain = INT_MIN;
    for(string x : atts){
        if(gain(regs,x) > maxgain){
            maxgain = gain(regs,x);
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
pair<vector<string>,int> get_classes(vector<int> regs){
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
string predominant_class(vector<int> regs){
    pair<vector<string>,int> x = get_classes(regs);
    return x.first[x.second];
}
double errortx(string pc,vector<int>regs){
    double err =0;
    for(int i:regs){
        if(base[i][base.size()-1]!=pc) err++;
    }
    return err/(double)regs.size();
}
class Node{
    public:
    vector<Node*> children;
    string classification;
    string attribute,value;
    double error_tx;
    Node(){
       this->children.resize(0);
    }
    Node(string classification,double error_tx){
        this->classification = classification;
        this->error_tx = error_tx;
        this->attribute="";
        this->value="";
    }
    Node(string value,string attribute){
        this->value = value;
        this->attribute = attribute;
        this->classification = "";
        this->error_tx=0.0;
    }
    vector<string> removefrom(vector<string> v,string s){
        vector<string> n_v;
        for(auto str:v) if(str!=s) n_v.push_back(str);
        return n_v;
    }
    vector<int> remove_not_equals(vector<int> v,int index,string value){
        vector<int> n_v;
        for(auto i:v) if(base[i][index]==value) n_v.push_back(i);
        return n_v;
    }
    void generate_node(vector<int> registers,vector<string> attributes){
        vector<string> classes = get_classes(registers).first;
        if(classes.size()>1){
            if(attributes.size()>0){
                string attribute = max_gain(registers,attributes);
                int index = indexof(or_atts,attribute);
                vector<string> values;
                for(int x : registers){
                    if(!contains(values,base[x][index])) values.push_back(base[x][index]);
                }
                for(auto value:values){
                    auto n = new Node(value,attribute);
                    children.push_back(n);
                    auto n_attributes = removefrom(attributes,attribute);
                    auto n_regs = remove_from(regs,index,value);
                    n->generate_node(n_regs,n_attributes);
                }
            }else{
                string pc = predominant_class(registers);
                double tx = errortx(pc,registers);
                this->classification = pc;
                this->error_tx = tx;
            }
        }else{
            this->classification=classes[0];
            this->error_tx=0.0;
        }
    }
};
int main(){
    return 0;
}