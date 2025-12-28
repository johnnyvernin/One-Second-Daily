# 📹 One Second Daily

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![MinSDK](https://img.shields.io/badge/MinSDK-24-orange.svg)

*Capture um segundo de cada dia e crie sua retrospectiva anual perfeita*

[Funcionalidades](#-funcionalidades) • [Instalação](#-instalação) • [Como Usar](#-como-usar) • [Tecnologias](#-tecnologias) • [Contribuindo](#-contribuindo)

</div>

---

## 🎯 Sobre o Projeto

**One Second Daily** é um aplicativo Android minimalista que te ajuda a criar vídeos retrospectivos incríveis do seu ano. Inspirado no conceito de gravar um segundo de cada dia, este app torna o processo simples e organizado.

### 💡 A Ideia

Imagine ter um vídeo de 6 minutos que resume seu ano inteiro - 365 dias, 365 momentos únicos. Este app foi criado para facilitar essa jornada:

- 📱 Grave **exatamente 1 segundo** por dia
- 🗂️ Vídeos organizados **automaticamente** em ordem cronológica
- 🎬 No final do ano, importe tudo para um editor (CapCut, Premiere, etc.)
- 🎵 Adicione uma música e tenha sua **retrospectiva perfeita**!

### 🎨 Por que este app?

Existem apps similares, mas este foi desenvolvido com foco em:
- ✅ **Simplicidade** - Sem recursos desnecessários
- ✅ **Controle total** - Seus vídeos ficam no armazenamento do celular
- ✅ **Gratuito e sem anúncios** - Código aberto, sem pegadinhas
- ✅ **Organização automática** - Nomenclatura pensada para edição fácil

---

## ✨ Funcionalidades

### 📹 Gravação
- Gravação automática de **1 segundo exato**
- Suporte para **câmera frontal e traseira**
- Qualidade **Full HD (1080p)** com fallback automático
- **Uma gravação por dia** - mantém a consistência

### 🔔 Lembretes
- **3 notificações diárias** (9h, 15h, 20h)
- Notificações inteligentes (não aparecem se já gravou)
- Incentivo para manter a rotina

### 🗂️ Organização
- Vídeos salvos em `DCIM/OneSecondDaily/`
- Nomenclatura cronológica: `YYYYMMDD_HHMMSS.mp4`
- Arquivos já ordenados alfabeticamente = ordem cronológica perfeita
- Compatível com qualquer editor de vídeo

### 🛠️ Controles
- **Delete e regrave** - Não gostou? Grave de novo!
- **Detecção inteligente** - Se deletar na galeria, o app percebe
- **Limpeza automática** - Remove arquivos temporários/corrompidos
- **Galeria integrada** - Visualize todos os seus vídeos no app

---

## 📲 Instalação

### Pré-requisitos
- Android Studio Arctic Fox ou superior
- Android SDK 24+ (Android 7.0+)
- Kotlin 1.8+

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/johnnyvernin/One-Second-Daily.git
cd One-Second-Daily
```

2. **Abra no Android Studio**
- File → Open → Selecione a pasta do projeto

3. **Sync do Gradle**
- O Android Studio vai sincronizar automaticamente
- Aguarde o download das dependências

4. **Compile e instale**
- Build → Build Bundle(s) / APK(s) → Build APK(s)
- Ou conecte seu celular e clique em Run ▶️

### Download do APK (Futuramente)
> Em breve você poderá baixar o APK pronto na aba [Releases](../../releases)

---

## 🎬 Como Usar

### Primeira vez
1. Abra o app e **conceda as permissões** (câmera, áudio, armazenamento, notificações)
2. Pronto! Já pode gravar seu primeiro segundo

### Rotina diária
1. Abra o app (ou clique na notificação)
2. Aperte o botão **vermelho de gravar**
3. O app grava automaticamente por 1 segundo
4. Pronto! Vídeo salvo e organizado ✓

### Trocar câmera
- Toque no ícone de **rotação** no canto superior direito
- Alterna entre câmera frontal e traseira

### Regravar o dia
- Se já gravou mas quer refazer:
  - Toque no ícone de **lixeira** na tela da câmera
  - Confirme e grave novamente

### Ver seus vídeos
- Toque na aba **"Galeria"**
- Veja todos os vídeos organizados por data
- Toque em um vídeo para reproduzir

### Criar a retrospectiva (final do ano)
1. Conecte o celular no PC
2. Copie todos os arquivos de `DCIM/OneSecondDaily/`
3. Abra seu editor favorito (CapCut, Premiere, DaVinci Resolve)
4. Importe todos os vídeos **em ordem**
5. Adicione uma música de fundo
6. Exporte e compartilhe! 🎉

---

## 🏗️ Estrutura do Projeto

```
OneSecondDaily/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/onesecond/daily/
│   │       │   ├── MainActivity.kt              # Tela principal e tabs
│   │       │   ├── CameraFragment.kt            # Gravação de vídeo
│   │       │   ├── GalleryFragment.kt           # Galeria de vídeos
│   │       │   ├── NotificationHelper.kt        # Sistema de notificações
│   │       │   ├── NotificationReceiver.kt      # Recebe notificações
│   │       │   ├── PreferenceHelper.kt          # Controle de gravações
│   │       │   └── MediaScannerHelper.kt        # Atualiza galeria
│   │       ├── res/
│   │       │   ├── layout/                      # Layouts XML
│   │       │   ├── drawable/                    # Ícones e recursos
│   │       │   ├── values/                      # Cores, strings, temas
│   │       │   └── xml/                         # Configurações
│   │       └── AndroidManifest.xml              # Permissões e config
│   └── build.gradle.kts                         # Dependências
```

---

## 🛠️ Tecnologias

### Principais
- **Kotlin** - Linguagem moderna e concisa
- **CameraX** - API de câmera poderosa e fácil
- **AndroidX** - Componentes modernos do Android

### Bibliotecas
- `androidx.camera:camera-*` (1.3.1) - Gravação de vídeo
- `com.google.android.material` - Material Design
- `androidx.constraintlayout` - Layouts responsivos

### Recursos
- **AlarmManager** - Notificações diárias
- **SharedPreferences** - Armazenamento local
- **MediaScanner** - Integração com galeria do sistema
- **FileProvider** - Compartilhamento seguro de arquivos

---

## ⚙️ Configurações Avançadas

### Mudar qualidade de vídeo

No arquivo `CameraFragment.kt`, localize:

```kotlin
val qualitySelector = QualitySelector.fromOrderedList(
    listOf(Quality.FHD, Quality.HD, Quality.SD),
    FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
)
```

**Para 4K:**
```kotlin
listOf(Quality.UHD, Quality.FHD, Quality.HD)
```

**Para 720p:**
```kotlin
listOf(Quality.HD, Quality.SD)
```

### Mudar horários das notificações

No arquivo `NotificationHelper.kt`:

```kotlin
// Manhã - 9h
scheduleNotification(context, alarmManager, 9, 0, MORNING_REQUEST_CODE)

// Tarde - 15h  
scheduleNotification(context, alarmManager, 15, 0, AFTERNOON_REQUEST_CODE)

// Noite - 20h
scheduleNotification(context, alarmManager, 20, 0, EVENING_REQUEST_CODE)
```

---

## 📊 Estimativas de Armazenamento

| Qualidade | 1 dia | 1 mês | 1 ano |
|-----------|-------|-------|-------|
| **4K (UHD)** | 2-4 MB | 60-120 MB | 730 MB - 1.5 GB |
| **Full HD** ⭐ | 1-2 MB | 30-60 MB | 365-730 MB |
| **HD (720p)** | 0.5-1 MB | 15-30 MB | 180-365 MB |
| **SD (480p)** | 0.2-0.5 MB | 6-15 MB | 70-180 MB |

> ⭐ **Recomendado:** Full HD oferece o melhor equilíbrio

---

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! 

### Como contribuir

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/MinhaFeature`)
3. **Commit** suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. **Push** para a branch (`git push origin feature/MinhaFeature`)
5. Abra um **Pull Request**

### Ideias de contribuição
- 🌍 Tradução para outros idiomas
- 🎨 Temas customizáveis (modo escuro/claro)
- 📤 Exportação direta para editor de vídeo
- ☁️ Backup na nuvem (opcional)
- 📊 Estatísticas e streak de dias gravados

---

## 📝 Roadmap

- [ ] Widget para gravar direto da home
- [ ] Preview do vídeo antes de salvar
- [ ] Filtros e efeitos rápidos
- [ ] Integração com Google Photos
- [ ] Exportação automática (concatenar vídeos)
- [ ] Suporte a múltiplos projetos/anos
- [ ] Compartilhamento direto nas redes sociais

---

## 🐛 Problemas Conhecidos

- Em alguns dispositivos Xiaomi, as notificações podem não funcionar (requer permissão de autostart)
- Câmera frontal em alguns modelos tem qualidade limitada a 720p

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

```
MIT License

Copyright (c) 2024 [Seu Nome]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

## 👤 Autor

**[Seu Nome]**

- GitHub: [@seu-usuario](https://github.com/johnnyvernin)
- LinkedIn: [Seu Perfil](https://linkedin.com/in/johnnyvernin)

---

## 🙏 Agradecimentos

- Inspirado no app "1 Second Everyday"
- Comunidade Android Developers
- Todos os contribuidores deste projeto

---

## 📸 Screenshots

> Adicione aqui screenshots do app quando estiver pronto!

---

<div align="center">

**⭐ Se este projeto te ajudou, deixe uma estrela! ⭐**

Feito com ❤️ e Kotlin

</div>
