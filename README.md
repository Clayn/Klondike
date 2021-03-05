# Klondike

---
## Description

Klondike is an application that manages other applications using an execution script. 
These scripts contain several steps for different tasks when launching the application (in fact 
the launch is just another optional step). 

These steps can be extended to provide a variety of possible actions to select from. 
The idea is, that you can either write your own steps or use some libraries from others that provide features you need.

## Design

Klondike basically has two modes how it can be used. 
- Shell mode
- Interactive mode

When using the shell mode, Klondike behaves as an application called from the command line. You 
can execute one task per call and may use it in you very own scripts.

When using the interactive mode, Klondike starts its internal command line (which provides only basic features such as escaping and quotes). 
On that command line you can use the same tasks as you can do in the shell mode. 
That way you may manage your applications manually without the need to start klondike all over again for each task.

At the moment all classes and interfaces are in one project, but due to the modular nature in future releases 
the public sdk classes may be moved to a separate project.

## Usage

Klondike provides several options and parameters to be used. This is only a small overview on what you may use.
All options are optional and only the parameters are required (starting the interactive mode, parameters are optional too)

`klondike [options] <arguments...>`

For the available options and arguments see... <TODO>

## Examples

All examples can be used either in shell mode or interactive mode. For the interactive mode 
you only have to omit the _klondike_ call after entering interactive mode.

#### Adding a new application

`klondike add <name> <executable>`

#### Starting an application

`klondike exec <name>`

#### Changing the name of an application

`klondike edit <name> name <NewName>`

#### Changing a step of the execution script

`klondike edit <name> step <index> <step-class> [-SP<parameter>=<value>...] [-C<parameter>=<typeclass>...]`

When using an invalid index (<0 or >current Step count) a new step will be added.

When the step defines some mandatory parameter they must be available in the call. The information about the type of the parameter is only mandatory if it is no parameter defined by the step. 
In that case the type is taken from the definition. If no type is given for a not defined parameter, _java.lang.String_ is used.

---

## Yukon

You may have noticed, that there is another project in the repository: **Yukon**

That is a GUI based on JavaFX to use Klondike. It basically imitates all necessary calls to the Klondike application. 
If you prefer to have a visual application, you can use Yukon instead of Klondike. 
Currently, it uses the internal classes, but the idea is that you can specify your klondike application to use and then Yukon will that instead. 