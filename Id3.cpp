#include <bits/stdc++.h>
using namespace std;
vector < vector < string > > base;
template <typename t>
bool contains(vector<t> vet,t c){
    for(t x : vet) if(x==c) return true;
    return false;
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
double entropy_system(){
    
}
double entropy(string att,vector<int> atts){

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
int indexof(vector<string> v,string s){
    for(int i=0;i<(int)v.size();i++) if(v[i]==s) return i;
    return -1;
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
    vector<int> freq;
    freq.resize(regs.size());
    for(int i:regs){
        string classe = base[i][((int)base[i].size())-1];
        if(!contains(ret,class)){
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
double error_tx(string pc){

}
class Node{
    public:
    vector<Node*> children;
    string classification;
    double error_tx;
    Node(){
       this->children.resize(0);
    }
    Node(string classification,double error_tx){
        this->classification = classification;
        this->error_tx = error_tx;
    }
    generate_node(vector<int> registers,vector<string> attributes){
        vector<string> classes = get_classes(registers).first;
        if(classes.size()>1){
            if(attributes.size()>0){
                string attribute = max_entropy(registers,attributes);
                //for values in attribute node = new Node(),node.generate_node(registers(value),attributes - {attribute});
            }else{
                children.push_back(new Node(predominant_class(registers),error_tx(predominant_class(registers))));
            }
        }else{
            children.push_back(new Node(classes[0],0.0));
        }
    }
};
int main(){
    return 0;
}