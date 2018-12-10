## Tawe-Lib

Implementation of a library platform for CS-230 at Swansea University

## Ideas for Extensive Features

- Add features & tools to the drawing / avatar class (i.e. eraser, paintbrush, undo button, etc.) 
- Check for wear and tear of resource; should be done every ~50 borrows
- Profile for users with ability to see and edit information
- Ability to delete users (only if they don't have any items on loan)
- Ability for librarians to search through all users like you can while browsing resources
- Reviews of resources

## Known issues

- Need to deal with the same resource being added (should it add to copy table?)
- There's nothing checking whether two resources are the same for DVD (check based on title and year?)
- Modularise all the duplicated code we have (particuarly changing scene methods)
