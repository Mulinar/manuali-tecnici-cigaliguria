PROGETTO ANDROID – MANUALI TECNICI CIGALIGURIA

Questo archivio contiene il progetto sorgente Android (non è ancora un APK).
La schermata dell'app è inclusa in app/src/main/assets/index.html.

CORREZIONE COMPILAZIONE GITHUB
Il workflow .github/workflows/build-apk.yml è stato corretto: ora cerca automaticamente
settings.gradle anche se GitHub trova il progetto dentro una sottocartella. Questo risolve
l'errore: "Directory ... does not contain a Gradle build".

COME GENERARE L'APK CON GITHUB
1. Estrai questo ZIP.
2. Nel repository GitHub manuali-tecnici carica i FILE E LE CARTELLE contenuti nello ZIP,
   mantenendo .github/workflows/build-apk.yml. Non caricare lo ZIP come singolo file.
3. Apri la scheda Actions.
4. Seleziona "Compila APK Android" e premi "Run workflow" (se richiesto scegli il ramo main).
5. Attendi che il workflow diventi verde.
6. Apri la compilazione completata e scarica l'artifact
   "Manuali-Tecnici-CIGALIGURIA-APK".
7. Apri lo ZIP scaricato: contiene Manuali-Tecnici-CIGALIGURIA.apk.
   Sul telefono Android apri l'APK e autorizza l'installazione da questa origine se richiesto.

NOTA: l'APK è il programma. I manuali PDF caricati nell'archivio locale della vecchia app
non vengono trasferiti automaticamente in questa nuova installazione. Conserva un backup
esportato dalla vecchia app prima di sostituirla.
