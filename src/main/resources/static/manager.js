const app = Vue.createApp({
    data() {
        return {
            data: [],
            clients: [],
            url: 'http://127.0.0.1:8080/clients',
            firstName: '',
            lastName: '',
            email: '',
            newClients: {},
            firstNameError: '',
            lastNameError: '',
            emailError: '',
            urlClient: '',
            disabledButton: true
        }
    },

    created() {
        this.loadData(this.url)
    },

    methods: {
        loadData(url) {
            axios.get(url).then(response => {
                this.data = response.data
                this.clients = this.data._embedded.clients
            })
        },

        addClient() {
            if(this.firstName !=='' && this.lastName !=='' && this.email !==''){

                this.newClients =   {
                                        "firstName": this.firstName, 
                                        "lastName": this.lastName, 
                                        "email": this.email
                                    }

                this.postClient(this.newClients)

                this.resetInput()
            } 
            
        },

        postClient(object) {
            axios.post(this.url, object).then(() => {
                this.loadData(this.url)
            })
        },

        getClientTable(client) {
            this.firstName = client.firstName
            this.lastName = client.lastName
            this.email = client.email
            this.disabledButton = false

            this.urlClient = client._links.self.href
        },

        updateClient(){

            this.newClients =   {
                "firstName": this.firstName, 
                "lastName": this.lastName, 
                "email": this.email
            }

            axios.patch(this.urlClient, this.newClients)
                .then(()=>{
                    this.loadData(this.url)  
            })

            this.resetInput()

            this.clearButton()
        },

        deleteClient(client){

            axios.delete(client._links.self.href)
                .then(()=>{
                    this.loadData(this.url)
                })
        },

        clearButton() {
            this.disabledButton = true
            this.resetInput()
        },

        resetInput(){
            this.firstName = ''
            this.lastName = ''
            this.email = ''
        },

        unfilled(){
            if(this.firstName === ''){
                this.firstNameError = 'Nombre debe rellenarse'
            } else{
                this.firstNameError = ''
            }

            if(this.lastName === ''){
                this.lastNameError = 'Apellido debe rellenarse'
            } else{
                this.lastNameError = ''
            }

            if(this.email === ''){
                this.emailError = 'Correo debe rellenarse'
            } else{
                this.emailError = ''
            }
        }
    }, 

    computed: {}


}).mount('#app')
