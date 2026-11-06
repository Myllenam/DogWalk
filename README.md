# DogWalk 🐶🚶

Aplicativo Android desenvolvido para a disciplina de **Desenvolvimento para Dispositivos Móveis**, com o objetivo de registrar e acompanhar passeios realizados com cães.

O DogWalk permite cadastrar pets, adicionar uma foto utilizando a câmera do dispositivo, iniciar passeios com acompanhamento por localização, consultar métricas do passeio e visualizar o histórico de atividades realizadas.

---

## 📱 Sobre o projeto

O **DogWalk** é um rastreador de passeios para cães.

A proposta do aplicativo é permitir que o usuário:

- cadastre seus pets;
- tire uma foto do animal utilizando a câmera;
- selecione um pet para iniciar um passeio;
- acompanhe o passeio utilizando localização e Open Street Map;
- visualize duração, distância e quantidade de passos;
- finalize e salve os passeios;
- consulte o histórico de passeios realizados;
- visualize os detalhes de cada passeio.

O projeto foi desenvolvido utilizando arquitetura **MVVM**, separando a interface, regras da tela e acesso aos dados.

---

# 🛠️ Tecnologias utilizadas

- Kotlin
- Android Studio
- XML
- Fragments
- Material Design
- RecyclerView
- Adapter
- MVVM
- Room
- Retrofit
- Dog CEO API
- Open Street Map
- GPS / serviços de localização
- Câmera do dispositivo
- Coroutines
- LiveData / Flow

---

# 🏗️ Arquitetura

O projeto utiliza o padrão arquitetural **MVVM — Model View ViewModel**.

A comunicação principal segue a estrutura:

```text
View
Fragment + XML
        ↓
ViewModel
        ↓
Repository
      ↙     ↘
   Room    Retrofit
```

Para funcionalidades relacionadas à localização:

```text
GPS / Localização
        ↓
WalkViewModel
        ↓
WalkFragment
        ↓
Open Street Map
```

## View

Responsável pela interface e interação com o usuário.

Exemplos:

```text
HomeFragment
PetsFragment
PetFormFragment
StartWalkFragment
WalkFragment
HistoryFragment
WalkDetailsFragment
ProfileFragment
```

Os Fragments não acessam diretamente o banco de dados.

---

## ViewModel

Responsável por armazenar o estado das telas e intermediar a comunicação entre a View e os Repositories.

Exemplos:

```text
HomeViewModel
PetsViewModel
PetFormViewModel
WalkViewModel
HistoryViewModel
```

---

## Repository

Responsável pelo acesso às fontes de dados.

Exemplo:

```text
PetFormFragment
       ↓
PetFormViewModel
       ↓
PetRepository
      ↙          ↘
    Room       Retrofit
```

---

# 💾 Persistência e integração

O trabalho exige o uso de pelo menos duas alternativas de persistência ou integração.

No DogWalk foram escolhidas:

## Room

O Room é utilizado como banco de dados local do aplicativo.

Os dados armazenados incluem:

- pets;
- dados dos passeios;
- histórico de atividades.

Exemplo:

```text
Pet
-------------------------
id
name
breed
sex
birthDate
photoUri
```

A foto do animal não é armazenada diretamente no banco.

O arquivo da imagem é salvo no armazenamento interno do aplicativo e o Room mantém apenas o URI da imagem.

---

## Retrofit

O Retrofit é utilizado para consumir uma API externa de raças de cães.

A integração utiliza a **Dog CEO API** para carregar as raças disponíveis durante o cadastro ou edição de um pet.

Fluxo:

```text
Cadastro do Pet
       ↓
PetFormViewModel
       ↓
PetRepository
       ↓
Retrofit
       ↓
Dog CEO API
       ↓
Lista de raças
       ↓
Spinner
```

Isso evita manter uma lista fixa de raças dentro da aplicação.

---

# 📷 Câmera

A câmera do dispositivo é utilizada durante o cadastro ou edição de um pet.

O usuário pode:

1. acessar o formulário do pet;
2. selecionar a opção de câmera;
3. tirar uma foto;
4. visualizar a foto no perfil do pet;
5. salvar o cadastro.

Fluxo:

```text
PetFormFragment
      ↓
Câmera
      ↓
Arquivo da imagem
      ↓
URI
      ↓
Pet
      ↓
Room
```

---

# 🗺️ Open Street Map e GPS

Durante um passeio, o aplicativo utiliza os serviços de localização do dispositivo para acompanhar o usuário.

A tela de passeio apresenta:

- localização atual;
- mapa;
- percurso realizado;
- duração;
- distância percorrida;
- quantidade de passos.

Exemplo:

```text
Passeio iniciado

⏱ 00:23:17
📍 1,82 km
🚶 2.430 passos
```

Ao finalizar, os dados do passeio são armazenados para consulta posterior.

---

# 📋 Funcionalidades

## Home

Tela inicial do aplicativo contendo um resumo das atividades.

Informações previstas:

- quantidade de passeios;
- distância percorrida;
- duração das atividades;
- passos;
- último passeio realizado.

---

## Pets

Lista dos pets cadastrados utilizando:

```text
RecyclerView + PetAdapter
```

Cada item apresenta informações básicas do pet e permite abrir sua tela de edição.

---

## Cadastrar / Editar Pet

Permite informar:

- nome;
- raça;
- sexo;
- data de nascimento;
- foto.

As raças são obtidas através de uma API externa utilizando Retrofit.

A foto pode ser capturada utilizando a câmera do dispositivo.

---

## Iniciar Passeio

Permite selecionar qual pet participará do passeio.

Após a seleção, o aplicativo inicia a tela de acompanhamento.

---

## Passeio em andamento

Apresenta:

- Open Street Map;
- localização;
- trajeto;
- duração;
- distância;
- passos.

---

## Histórico

Lista os passeios já realizados utilizando:

```text
RecyclerView + WalkAdapter
```

Esse RecyclerView possui um contexto diferente da lista de pets, atendendo aos requisitos da disciplina.

---

## Detalhes do Passeio

Apresenta informações completas de uma atividade:

- pet;
- data;
- mapa;
- trajeto;
- duração;
- distância;
- passos;
- horário de início;
- horário de término.

---

# 🧭 Navegação

A aplicação utiliza **Fragments** e **Bottom Navigation**.

A navegação principal possui:

```text
Home
Pets
Histórico
Perfil
```

Telas secundárias são abertas a partir desses Fragments.

Exemplo:

```text
Home
 ↓
Iniciar passeio
 ↓
Selecionar Pet
 ↓
Passeio em andamento
 ↓
Finalizar
 ↓
Detalhes
```

Outro fluxo:

```text
Pets
 ↓
Lista de Pets
 ↓
Novo Pet / Editar Pet
 ↓
Câmera
 ↓
Salvar
```

---

# 📂 Estrutura do projeto

```text
com.example.dogwalk
│
├── data
│   ├── local
│   │   ├── dao
│   │   │   ├── PetDao.kt
│   │   │   └── WalkDao.kt
│   │   │
│   │   └── database
│   │       └── DogWalkDatabase.kt
│   │
│   ├── remote
│   │   ├── DogApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── BreedResponse.kt
│   │
│   └── repository
│       ├── PetRepository.kt
│       └── WalkRepository.kt
│
├── model
│   ├── Pet.kt
│   ├── Walk.kt
│   └── WalkLocation.kt
│
├── ui
│   ├── home
│   │   ├── HomeFragment.kt
│   │   └── HomeViewModel.kt
│   │
│   ├── pets
│   │   ├── PetsFragment.kt
│   │   ├── PetsViewModel.kt
│   │   ├── PetFormFragment.kt
│   │   ├── PetFormViewModel.kt
│   │   └── PetAdapter.kt
│   │
│   ├── walk
│   │   ├── StartWalkFragment.kt
│   │   ├── WalkFragment.kt
│   │   └── WalkViewModel.kt
│   │
│   ├── history
│   │   ├── HistoryFragment.kt
│   │   ├── HistoryViewModel.kt
│   │   ├── WalkDetailsFragment.kt
│   │   └── WalkAdapter.kt
│   │
│   └── profile
│       ├── ProfileFragment.kt
│       └── ProfileViewModel.kt
│
└── MainActivity.kt
```

---

# 👥 Divisão do desenvolvimento

O projeto foi desenvolvido em dupla, com as funcionalidades separadas por domínio para permitir o desenvolvimento paralelo.

## Parte 1 — Pets

Responsabilidades principais:

- tela de pets;
- RecyclerView de pets;
- PetAdapter;
- cadastro de pet;
- edição de pet;
- integração com câmera;
- armazenamento da foto;
- integração com API de raças;
- Retrofit;
- PetViewModel;
- PetFormViewModel;
- PetRepository;
- PetDao;
- persistência dos pets utilizando Room.

Fluxo principal:

```text
Pets
 ↓
Cadastrar / Editar
 ↓
Câmera
 ↓
Retrofit / Raças
 ↓
ViewModel
 ↓
Repository
 ↓
Room
```

---

## Parte 2 — Passeios

Responsabilidades principais:

- seleção do pet;
- início do passeio;
- Open Street Map;
- localização GPS;
- acompanhamento do trajeto;
- cronômetro;
- distância;
- passos;
- finalização do passeio;
- persistência dos passeios;
- histórico;
- WalkAdapter;
- detalhes do passeio;
- WalkViewModel;
- WalkRepository;
- WalkDao.

Fluxo principal:

```text
Selecionar Pet
      ↓
Iniciar Passeio
      ↓
Open Street Map + GPS
      ↓
Tempo / Distância / Passos
      ↓
Finalizar
      ↓
Room
      ↓
Histórico
```

---



# ✅ Requisitos da disciplina

| Requisito | Implementação no DogWalk |
|---|---|
| Tema livre | DogWalk |
| Pelo menos 6 telas | ✅ |
| Componentes gráficos | ✅ |
| RecyclerView + Adapter | ✅ Pets |
| Segundo RecyclerView + Adapter | ✅ Histórico |
| Fragments | ✅ |
| Bottom Navigation | ✅ |
| MVVM | ✅ |
| Persistência 1 | ✅ Room |
| Integração 2 | ✅ Retrofit |
| Item especial 1 | ✅ Google Maps |
| Item especial 2 | ✅ Câmera |

---

# 🚀 Como executar

1. Clonar o repositório:

```bash
git clone git@github.com:myllenam/DogWalk.git
```

2. Abrir o projeto no Android Studio.

3. Sincronizar as dependências Gradle.

4. Configurar a chave necessária para utilização do Google Maps, caso a funcionalidade já esteja habilitada.

5. Executar em um emulador ou dispositivo Android.

Para testar corretamente câmera e localização, recomenda-se utilizar um dispositivo físico quando possível.

---

# 📌 Objetivo acadêmico

O projeto foi desenvolvido para aplicar conceitos estudados na disciplina de Desenvolvimento para Dispositivos Móveis, incluindo:

- desenvolvimento Android nativo;
- Kotlin;
- arquitetura MVVM;
- navegação utilizando Fragments;
- RecyclerView e Adapter;
- persistência local com Room;
- integração com API externa utilizando Retrofit;
- utilização da câmera;
- utilização de localização e Google Maps.

