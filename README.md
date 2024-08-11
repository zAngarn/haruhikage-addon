# Ornithe Carpet Extension Tamplate

The Ornithe Carpet Extension Tamplate mod. 

You can use it as a template to make an [ornithe-carpet](https://github.com/CrazyHPi/ornithe-carpet) extension mod.

## Usage

1. Create a repository from this template, then clone it to your PC.
2. Update `gradle.properties` and `fabric.mod.json` to match your mod id.
3. If you wish to use [osl](https://github.com/OrnitheMC/ornithe-standard-libraries) aka fabric-api for ornithe, remove relevent comments is `gradle.properties` and `build.gradle`.
4. Good to go!

### Setting Up IDEA Dev Environment

* Change gradle JVM to Java17+, set `Compiler Output` in Project Structure Settings to somewhere you like
* Project SDK should be Java8
* Recommending fabric loader is 0.13.3, fabric loader ver >= 0.14.0 will crash in dev env if using Java8
* For [Mixin Extras](https://github.com/LlamaLad7/MixinExtras) support, reference [this commit](https://github.com/CrazyHPi/Ornithe-Carpet-Extra/commit/c48f71d761ffaed3052a591b469807527d79e40b), ~~or use fabric loader 0.15+ which has bundled mixin extras~~
* Check out [here](https://fabricmc.net/wiki/tutorial:mixin_hotswaps) for better debugging experience