// Canibais&Missionarios.cpp : Este arquivo contém a função 'main'. A execução do programa começa e termina ali.
//

#include <iostream>
#include <vector>
#include <queue>
#include <string>
using namespace std;

class par {
	int missionarios;
	int canibais;
public:
	int Missionarios() {
		return this->missionarios;
	}
	int Canibais() {
		return this->canibais;
	}
	void Missionarios(int m) {
		this->missionarios = m;
	}
	void Canibais(int c) {
		this->canibais = c;
	}
	par(int c,int m) {
		Canibais(c);
		Missionarios(m);
	}
	par(pair<int, int> cm) {
		Canibais(cm.first);
		Missionarios(cm.second);
	}
};
class Margens {
public:
	par* MargemDireita;
	par* MargemEsquerda;
	Margens(int esqc,int esqm, int dirc,int dirm) {
		MargemDireita = new par(dirc, dirm);
		MargemEsquerda = new par(esqc, esqm);
	}
	Margens(pair<int, int> esquerda, pair<int, int> direita) {
		MargemEsquerda = new par(esquerda);
		MargemDireita = new par(direita);
	}
};
class Node {
public:
	Node* pai;
	vector<Node*> Filhos;
	Margens* estado;
	int depth, cost;
	string message;
	Node(Margens *estado) {
		this->estado = estado;
		pai = NULL;
		depth = 0;
		cost = 1;
		message = "Estado Inicial";
	}
	Node(Margens* estado, Node* pai,string message) {
		this->estado = estado;
		this->pai = pai;
		depth = pai->depth + 1;
		cost = 1;
		this->message = message;
	}
	void GerarFilhos() {
		//1° Possibilidade ,levar 1 canibal e um missionario para a margem direita , se for possivel
		int CanibEsq = estado->MargemEsquerda->Canibais();
		int CanibDir = estado->MargemDireita->Canibais();
		int MissEsq = estado->MargemEsquerda->Missionarios();
		int MissDir = estado->MargemDireita->Missionarios();
		if (CanibEsq != 0 && MissEsq != 0) {
			CanibEsq--;
			MissEsq--;
			MissDir++;
			CanibDir++;
			AddFilho(CanibEsq, MissEsq, CanibDir, MissDir,"Um Canibal e um Missionario para direita ,");
		}
		CanibEsq = estado->MargemEsquerda->Canibais();
		CanibDir = estado->MargemDireita->Canibais();
		MissEsq = estado->MargemEsquerda->Missionarios();
		MissDir = estado->MargemDireita->Missionarios();
		if (CanibEsq > 1) {
			CanibEsq -= 2;
			CanibDir += 2;
			AddFilho(CanibEsq, MissEsq, CanibDir, MissDir,"Dois Canibais para direita ,");
		}
		CanibEsq = estado->MargemEsquerda->Canibais();
		CanibDir = estado->MargemDireita->Canibais();
		MissEsq = estado->MargemEsquerda->Missionarios();
		MissDir = estado->MargemDireita->Missionarios();
		if (MissEsq > 1) {
			MissEsq -= 2;
			MissDir += 2;
			AddFilho(CanibEsq, MissEsq, CanibDir, MissDir,"Dois Missionarios para direita ,");
		}
		CanibEsq = estado->MargemEsquerda->Canibais();
		CanibDir = estado->MargemDireita->Canibais();
		MissEsq = estado->MargemEsquerda->Missionarios();
		MissDir = estado->MargemDireita->Missionarios();
		if (CanibEsq == 1) {
			CanibDir++;
			CanibEsq--;
			AddFilho(CanibEsq, MissEsq, CanibDir, MissDir, "Um Canibal para direita ,");
		}
		CanibEsq = estado->MargemEsquerda->Canibais();
		CanibDir = estado->MargemDireita->Canibais();
		MissEsq = estado->MargemEsquerda->Missionarios();
		MissDir = estado->MargemDireita->Missionarios();
		if (MissEsq == 1) {
			MissEsq--;
			MissDir++;
			AddFilho(CanibEsq, MissEsq, CanibDir, MissDir, "Um Missionario para direita ,");
		}
	}
	void AddFilho(int CanibEsq, int MissEsq, int CanibDir, int MissDir,string message) {
		if (!((CanibEsq > MissEsq&& MissEsq != 0) || (CanibDir > MissDir&& MissDir != 0))) {
			//Conseguiu
			if (CanibDir == 3 && MissDir == 3 && CanibEsq == 0 && MissEsq == 0) 
			Filhos.push_back(new Node(new Margens(CanibEsq, MissEsq, CanibDir, MissDir), this, " Nenhum voltou"));
			//Voltar Um Canibal
			if (!((CanibEsq+1 > MissEsq&& MissEsq != 0) || (CanibDir-1 > MissDir&& MissDir != 0)) && CanibDir!=0 && (CanibDir-1 != estado->MargemDireita->Canibais() || CanibEsq+1!=estado->MargemEsquerda->Canibais()||MissDir!=estado->MargemDireita->Missionarios()||MissEsq!=estado->MargemEsquerda->Missionarios()))
			Filhos.push_back(new Node(new Margens(CanibEsq+1, MissEsq, CanibDir-1, MissDir), this,message +" Voltando Um Canibal"));
			//Voltar Um Missionario
			if (!((CanibEsq > MissEsq+1&& MissEsq+1 != 0) || (CanibDir > MissDir-1&& MissDir-1 != 0))&&MissDir!=0 && (CanibDir != estado->MargemDireita->Canibais() || CanibEsq != estado->MargemEsquerda->Canibais() || MissDir-1 != estado->MargemDireita->Missionarios() || MissEsq+1 != estado->MargemEsquerda->Missionarios()))
			Filhos.push_back(new Node(new Margens(CanibEsq, MissEsq + 1, CanibDir, MissDir -1), this,message + " Voltando Um missionario"));
			//Voltar Dois Canibais
			if (CanibDir > 1 && (CanibEsq + 2 <= MissEsq && MissEsq != 0) && (CanibDir - 2 != estado->MargemDireita->Canibais() || CanibEsq + 2 != estado->MargemEsquerda->Canibais() || MissDir != estado->MargemDireita->Missionarios() || MissEsq != estado->MargemEsquerda->Missionarios())) 
				Filhos.push_back(new Node(new Margens(CanibEsq+2, MissEsq , CanibDir-2, MissDir ), this, message + " Voltando Dois Canibais"));
			//Voltar Dois Missionarios
			if (MissDir > 1 && (CanibDir <= MissDir - 2 && MissDir - 2 != 0) && (CanibDir != estado->MargemDireita->Canibais() || CanibEsq != estado->MargemEsquerda->Canibais() || MissDir - 2 != estado->MargemDireita->Missionarios() || MissEsq + 2 != estado->MargemEsquerda->Missionarios()))
				Filhos.push_back(new Node(new Margens(CanibEsq, MissEsq + 2, CanibDir, MissDir - 2), this, message + " Voltando Dois missionarios"));
			//Voltar Um Canibal e Um Missionario
			if((CanibDir>=1&&MissDir>=1) && CanibDir-1<=MissDir-1 && MissDir!=0 && (CanibDir-1 != estado->MargemDireita->Canibais() || CanibEsq +1!= estado->MargemEsquerda->Canibais() || MissDir - 1 != estado->MargemDireita->Missionarios() || MissEsq + 1 != estado->MargemEsquerda->Missionarios()))
				Filhos.push_back(new Node(new Margens(CanibEsq+1, MissEsq + 1, CanibDir-1, MissDir - 1), this, message + " Voltando Um missionario e Um Canibal"));
		}
	}
};
ostream& operator<<(ostream& o,par p) {
	return o << "( " << p.Canibais() << " , " << p.Missionarios() << " )";
}
ostream& operator<<(ostream& o, Margens e) {
	return o << "[" << *e.MargemEsquerda << " , " << *e.MargemDireita << " ]";
}

void BackTracking(Node* n) {
	if (n != NULL) {
		BackTracking(n->pai);
		cout << (*n->estado) << endl;
	}
}
void BFS(Node* start) {
	queue<Node*> q;
	q.push(start);
	while (!q.empty()) {
		Node* deq = q.front();
		q.pop();
		if (deq->estado->MargemDireita->Canibais() == 3 && deq->estado->MargemDireita->Missionarios() == 3) {
			cout << "Encontrada Solucao\n";
			BackTracking(deq);
			break;
		}
		deq->GerarFilhos();
		for (int i = 0; i < deq->Filhos.size();i++) {
			Node* f = deq->Filhos[i];
			q.push(f);
		}
	}
}
int main() {
	Node MissECan(new Margens(3, 3, 0, 0));
	BFS(&MissECan);
	return 0;
}

