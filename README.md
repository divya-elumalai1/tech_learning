# LLM Image Generator

A small Java app that turns text you enter into a PNG badge. A local language model decides to call a Java method, and that method draws the image.

The model does not create the pixels. `ImageGeneratorTools` draws the badge with `Graphics2D` and saves `generated_badge.png` in the project folder.

## Requirements

- Java 17
- Maven
- [Ollama](https://ollama.com) running locally, with the `llama3.2` model

```bash
ollama pull llama3.2
ollama serve
```

Ollama listens on `http://localhost:11434`.

## Run

From the project root:

```bash
mvn exec:java -Dexec.mainClass="com.example.Main"
```

A dialog asks for the badge text. You can also pass the text on the command line:

```bash
mvn exec:java -Dexec.mainClass="com.example.Main" -Dexec.args="Hello from Cursor"
```

When the run finishes, the console prints the saved file path. Open that PNG. A web link in the model reply is not the image this program created.

## How it works

1. `Main` collects the badge text and builds a prompt.
2. LangChain4j sends the prompt to Ollama (`llama3.2`), along with the `generateTextImage` tool.
3. The model returns a tool call. LangChain4j runs `ImageGeneratorTools.generateTextImage`.
4. The method draws a 640×220 badge and writes `generated_badge.png`.
5. The tool result goes back to the model, which replies with the local path.

## Project layout

```text
pom.xml
src/main/java/com/example/
├── Main.java                 # input, Ollama setup, and output
├── ImageAssistant.java       # chat interface implemented by LangChain4j
└── ImageGeneratorTools.java  # badge drawing and file output
```

## Dependencies

| Dependency | Purpose |
|---|---|
| `langchain4j` | Chat API, AI services, and tool calling |
| `langchain4j-ollama` | Connection to a local Ollama model |
| `slf4j-simple` | Console logging |
