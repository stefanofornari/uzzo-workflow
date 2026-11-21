# Create a simple calculator app

## First create a project

### From command line + AI CLI

#### Create the project folder and add the skills
1. doanload [uzzo-workflow-kb.jar](https://github.com/stefanofornari/uzzo-workflow/releases/download/uzzo-workflow-0.0.1-0.0.2/uzzo-workflow-kb-0.0.1.jar)
1. create a project directory `simple-calc`
1. unzip `uzzo-workflow-kb.jar!uzz/` into `simple-calc/`
1. add skills `uzz/skills` to your preferred LLM CLI (see [Installing Skills](../installing-skills.md))

#### Initialize the knowledge base
1. start your cli and invoke skill uzzo-init (usually with /uzzo-init or a prompt
   like `initialize the uzzo knowledge base`

### With NetBeans plugin

#### Create the project
1. go to Tools/Plugins/Installed
1. select Uzzo Workflow for NetBeans
    ![Uzzo Workflow Plugin](images/fig1.png)
1. install the pluing
1. create a standard maven project
   1. File/New Project/Java with Maven/Java Application
   1. provide the information:
       ![Uzzo Workflow Plugin](images/fig2.png)

#### Initialize the knowledge base
1. right click on simple-calc and select Initialize Uzzo Knowledge Base
2. provide the requested information or accept the proposed values (fig 4)

{{fig1}}
{{fig2}}
{{fig3}}
{{fig4}}

## Let's scope the project
Let's start with a simple prompt with a rough description of what we want.
Open the CLI and type something like
```
I want to build a simple calculator application that should run on windows, macos
and linux. It should have the following features:
- support basic arithmetics operations
- allow to use parentesis to group expressions
- show the current expression
- ENTER calculates the expression and show the result
- in case of errors (e.g. divide by 0 or unbalanced parentesis) show a simple error
- clean and modern look&fill
Let's scope the project.
```