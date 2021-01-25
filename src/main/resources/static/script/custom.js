const collapsedClassName = 'collapsed';

document.addEventListener("DOMContentLoaded", function(event) {
    setUpCollapsables();
  });

function setUpCollapsables(){
    let list = document.getElementsByClassName('collapsable');
    if(list){
        console.log(list.length);
        for(let i=0;i<list.length;i++){
            let collapsable = list[i];
            collapsable.classList.add(collapsedClassName);
            setUpOnClickListenerForCollapsable(collapsable);
        }
    }
}

function setUpOnClickListenerForCollapsable(collapsable){
    if(collapsable){
        let children = collapsable.childNodes;
        for(let i=0; i<children.length;i++){
            let classList = children[i].classList;
            if(classList){
                if(classList.contains('collapsable--header')){
                    let header = children[i];
                    header.addEventListener("click",()=>{
                                toggleCollapsedMode(collapsable);
                            });
                }
            }
        }
    }
}

function toggleCollapsedMode(element){
   if(element){
       if(element.classList){
         element.classList.toggle(collapsedClassName);
       }
   }
}