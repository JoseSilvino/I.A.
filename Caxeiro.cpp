#include <iostream>
#include <vector>
#include <queue>
#include <climits>
using namespace std;

class MyWay{
	int matriz[10][10];
public:
	int distance;
	vector<int> caminho;
	bool operator<(MyWay& n) {
		return distance < n.distance;
	}
	bool operator>=(MyWay& n) {
		return distance >= n.distance;
	}
	bool operator==(MyWay& m) {
		for (int i = 0; i < 11; i++) if (caminho[i] != m.caminho[i])return false;
			return true;
	}
	bool operator==(int *m) {
		for (int i = 0; i < 11; i++) if (caminho[i] != m[i])return false;
		return true;
	}
	int dist() {
		int count = 0;
		for (size_t i = 0; i < caminho.size()-1; i++) {
				int t = matriz[caminho[i]][caminho[i + 1]];
				count += t;
		}
		return count;
	}
	MyWay(int arr[11],int matriz[10][10]) {
		for (int i = 0; i < 11; i++) caminho.push_back(arr[i]);
		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++) this->matriz[i][j] = matriz[i][j];
		distance = dist();
	}
	void swap(int a, int b) {
		int c = caminho[a];
		caminho[a] = caminho[b];
		caminho[b] = c;
	}
	void setArr(int a[11]) {
		for (int i = 0; i < 11; i++) a[i] = caminho[i];
	}
	bool valido() {
		for (int i = 0; i < 10; i++) if (matriz[caminho[i]][caminho[i + 1]] < 1)return false;
		return true;
	}
};

MyWay min(MyWay a, MyWay b) {
	return (a.distance < b.distance) ? a : b;
}

void BFS(int start,int a[11], int graph[10][10]) {
	queue<int> q;
	q.push(start);
	bool vis[10] = {0};
	int j = 1;
	vis[start] = true;
	while (!q.empty()) {
		int deq = q.front();
		q.pop();
		for (int i = 0; i < 10;i++) {
			if (graph[deq][i] > 0&&!vis[i]) {
				vis[i] = true;
				q.push(i);
				a[j] = i;
				j++;
			}
		}
	}
}
void swap(int* a, int* b) {
	int c = *a;
	*a = *b;
	*b = c;
}

void tryLower(int pos[11], int dists[10][10], MyWay& max,int depth,int max_depth) {
		if (depth < max_depth) {
			for (int i = 1; i < 10; i++) for (int j = 1; j < 10; j++) {
				if (i != j) {
					int tmp[11];
					for (int t = 0; t < 11; t++)tmp[t] = pos[t];
					swap(&tmp[i], &tmp[j]);
					MyWay talvez(tmp, dists);
					if (talvez.valido()) {
						max = min(talvez, max);
					}
					tryLower(tmp, dists, max, depth + 1, max_depth);
				}
			}
		}
}
bool visited[10];
void DFS(int start,int path[11],int graph[10][10],int &ind){
	path[ind] = start;
	visited[start] = true;
	for(int i=0;i<10;i++){
		if(graph[i][start]>0&&!visited[i]){
			ind++;
			DFS(i,path,graph,ind);
		}
	}
}
int getIndex(MyWay way,int city){
	int i=0;
	for(auto x:way.caminho) {
		if(x==city) return i;
		i++;
	}
	return -1;
}
int main() {
	int matriz[10][10] = {
	{0, 30, 84, 56, -1, -1, -1, 75, -1, 80},
	{ 30, 0, 65 ,-1, -1,-1,70, -1, -1,40 } ,
	{ 84, 65, 0, 74, 52 ,55 ,-1 ,60 ,143, 48 },
	{ 56, -1, 74, 0, 135, -1, -1,20 ,-1, -1},
	{ -1,-1,52, 135, 0, 70, -1, 122, 98, 80 },
	{ 70, -1, 55, -1, 70, 0, 63, -1, 82, 35 },
	{ -1,70, -1, -1,-1,63, 0, -1, 120, 57 },
	{ 75, -1, 135, 20, 122, -1, -1,0, -1, -1},
	{ -1,-1,143, -1, 98, 82, 120, -1, 0, -1 },
	{ 80, 40, 48, -1, 80 ,35, 57, -1, -1,0 } };
	int comeco=0;
	while(comeco<1||comeco>10){
		cout<<"Digite uma cidade inicial entre 1 e 10\n";
		cin >> comeco;
	}
        comeco--;
	cout<<"Pode demorar alguns segundos , por favor tenha paciencia\n";
	cout<<"Cidade inicial: C"<<comeco+1<<endl;
	int a[11] = { 0 };
	a[0] = 0;
	a[10] = 0;
	BFS(0, a, matriz);
	MyWay max(a, matriz);
	max.distance = INT_MAX;
	int tmp[11];
	for (int n = 0; n < 6; n++) {
		max.setArr(tmp);
		int mp = max.distance;
		tryLower(tmp, matriz, max, 0,3);
		if(max.distance==mp) {
			break;
		}
	}
	cout <<"\nDistancia minima encontrada :"<< max.distance<<endl;
	cout<<"Caminho encontrado:\n";
	int comeco_index = getIndex(max,comeco);
	for(int i = comeco_index;i<11;i++) cout<<"C"<<max.caminho[i]+1<<" ";
	for(int i=1;i<=comeco_index;i++) cout<<"C"<<max.caminho[i]+1<<" ";
	cout << endl;
	return 0;
} 
