const app = Vue.createApp({
    data(){
        return{
            url:'http://127.0.0.1:8080/api/clients/current',
            client: [],
            cards: [],
            cardsCredit: [],
            cardsDebit: [],
            cardColor: '',
            cardType: '',
            cardId: '',
        }
    },

    created() {
        this.loadData(this.url)
    },

    methods: {
        loadData(url){
            axios.get(url).then(response =>{
                this.client = response.data
                this.cards = this.client.cards

                this.cards = this.cards.sort((a, b) => a.id - b.id)

                this.cardsCredit = this.cards.filter(element => element.type == 'CREDIT')
                this.cardsDebit = this.cards.filter(element => element.type == 'DEBIT')

                console.log(this.cardsCredit[0].id);
            })
            .catch(error => console.log(error));
        },

        dateSplit(date){       
            return dateString = date.split('-')[1] + '/' + date.split('-')[0].slice(-2)
        },

        numberSplit(number){
            return number.substr(0, 4) + ' ' + number.substr(4, 4) + ' ' + number.substr(8, 4) + ' ' + number.substr(12, 4)
        },

        logout(){
            axios.post('/api/logout').then(() => window.location.assign('../home/index.html'))
        },

        createCard(){
            axios.post('/api/clients/current/cards', `cardColor=${this.cardColor}&cardType=${this.cardType}`)
            .then(() => window.location.reload())
            .catch(error => {
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${errorData}`
                })
            })
        },

        deleteCard(str){
            axios.patch(`/api/clients/current/cards/delete/${str}`)
            .then(() => this.loadData(this.url))
            .catch(error => {
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${errorData}`
                })
            })
        },

        deleteCardButton(str){
            Swal.fire({
                title: 'Delete Card',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                if (result.isConfirmed) {
                    this.deleteCard(str)
                    Swal.fire(
                        'Deleted!',
                        'You have deleted the card.',
                        'success'
                        )   
                    }
                })
        }
    },
    
}).mount('#app')

// axios.post('/api/clients/current/cards', "cardColor=GOLD&cardType=CREDIT").then(console.log("Created") )